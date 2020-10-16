package org.example.judger.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.example.bean.JudgerResult;
import org.example.bean.RuntimeResult;
import org.example.bean.SubmissionMsg;
import org.example.judger.config.OjProperties;
import org.example.judger.exception.NoPassException;
import org.example.judger.message.MsgConst;
import org.example.judger.util.IOUtil;
import org.example.mapper.OjProblemsMapper;
import org.example.model.OjProblemCheckpoints;
import org.example.model.OjProblems;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/7/11 13:45
 * <p>
 * 用于调度每个测评任务
 */
@Component
@Slf4j
public class Dispatcher {

    @Autowired
    private Compiler compiler;
    @Autowired
    private PreProcess preProcess;
    @Autowired
    private Runner runtime;
    @Autowired
    private Comparator comparator;
    @Autowired
    private OjProperties ojProperties;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OjProblemsMapper ojProblemsMapper;

    /**
     * 处理接受到的submission消息
     *
     * @param msg 消息
     */
    public void onSubmissionCreated(SubmissionMsg msg) {
        try {
            String srcName = preProcess.preCompilerHandler(msg, ojProperties);
            if (StringUtil.isEmpty(srcName))
                throw new IllegalStateException("代码文件生成失败-submission[" + msg.getSubmissionId() + "]");
            Runner.RunTimeResult compileResult = compiler.compile(srcName, msg, runtime, ojProperties);
            if (compileResult.getRes() != RuntimeResult.AC) {
                onCompileFailureHandler(ojProperties, msg, compileResult);
                throw new IllegalStateException("编译文件失败-submission[" + msg.getSubmissionId() + "]");
            }
            List<OjProblemCheckpoints> checkpoints = preProcess.preRuntimeHandler(msg, ojProperties);
            if (checkpoints.isEmpty())
                throw new IllegalStateException("生成测试文件失败-submission[" + msg.getSubmissionId() + "]");
            runtimeProcess(checkpoints, srcName, msg, ojProperties);

        } catch (Exception e) {
            onExceptionThrow(e, msg);
        }finally {
            cleanUp(msg);
        }

    }

    private void onExceptionThrow(Exception e, SubmissionMsg msg) {
        log.error("submission[{}],{}", msg.getSubmissionId(), e.getMessage());
    }

    /**
     * 编译失败处理
     *
     * @param ojProperties
     * @param msg
     */
    private void onCompileFailureHandler(OjProperties ojProperties, SubmissionMsg msg, Runner.RunTimeResult compileResult) {
        JudgerResult judgerResult = new JudgerResult();
        judgerResult.setSubmission(msg);
        judgerResult.setRuntimeResult(RuntimeResult.CE);
        File compileLog = new File(ojProperties.getWorkPath(), "compileLog.txt");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtil.copy(outputStream, compileLog);
            judgerResult.setOutput(new String(outputStream.toByteArray()));
        } catch (IOException e) {
            judgerResult.setOutput("");
            throw new IllegalStateException("获取编译日志失败-submission[" + msg.getSubmissionId() + "]", e);
        } finally {
            judgerResult.setError("compiler error");
            rabbitTemplate.convertAndSend("oj.submission", "submission.event.result", judgerResult);
        }
    }

    private void cleanUp(SubmissionMsg msg) {
        File baseDirFile = new File(ojProperties.getWorkPath());
        if (baseDirFile.exists()) {
            try {
                FileUtils.deleteDirectory(baseDirFile);
            } catch (IOException ex) {
//                log.error("清理工作路径失败,submission[{}]", msg.getSubmissionId());
            }
        }
    }

    private void runtimeProcess(List<OjProblemCheckpoints> checkpoints, String srcName, SubmissionMsg msg, OjProperties ojProperties) {
        try {
            int useTime = 0;
            int userMem = 0;
            for (OjProblemCheckpoints checkpoint : checkpoints) {
                String runCommand = msg.getLanguage().getLanguageRunCommand()
                        .replace("{filename}", srcName.substring(0, srcName.lastIndexOf('.')));
                String input = ojProperties.getWorkPath() + String.format("/%s#input#%s#smp.txt",
                        checkpoint.getProblemId(), checkpoint.getProblemCheckpointId());
                String output = ojProperties.getWorkPath() + String.format("/%s#output#%s.txt",
                        checkpoint.getProblemId(), checkpoint.getProblemCheckpointId());
                String outputSamp = ojProperties.getWorkPath() + String.format("/%s#output#%s#smp.txt",
                        checkpoint.getProblemId(), checkpoint.getProblemCheckpointId());
                OjProblems ojProblems = ojProblemsMapper.selectByPrimaryKey(msg.getProblemId());

                //execution
                Map<String, Object> runtimeResult = runtime.getRuntimeResult(runCommand, ojProperties.getUserName(), ojProperties.getPassWord(),
                        input, output, ojProblems.getProblemTimeLimit(), ojProblems.getProblemMemoryLimit());
                Runner.RunTimeResult runTimeResult = runtime.transForm(runtimeResult, ojProblems.getProblemTimeLimit(), ojProblems.getProblemMemoryLimit());
                onCheckPointFinish(runTimeResult, outputSamp, output, msg, checkpoint.getProblemCheckpointId());
                useTime += runTimeResult.getUsedTime();
                userMem += runTimeResult.getUsedMemory();
            }
            onAllCheckPointFinish(msg, useTime, userMem);
        } catch (NoPassException ignored) {
        }
    }

    /**
     * 完成一个测试点
     *
     * @param runTimeResult
     * @param outputSamp
     * @param output
     * @param msg
     * @param checkPointId
     */
    private void onCheckPointFinish(Runner.RunTimeResult runTimeResult, String outputSamp, String output, SubmissionMsg msg, Integer checkPointId) {
        log.info("comparate--{}<=>{}-submission[{}]", outputSamp, output, msg.getSubmissionId());
        if (runTimeResult.getRes() == RuntimeResult.TLE||runTimeResult.getRes()==RuntimeResult.MLE) {
            log.warn("程序未能正确通过验证,submission[{}]-checkpoint[{}]", msg.getSubmissionId(), checkPointId);
            JudgerResult judgerResult = new JudgerResult();
            judgerResult.setError(runTimeResult.getRes()==RuntimeResult.TLE?"time limit error":"memory limit error");
            judgerResult.setOutput("");
            judgerResult.setSubmission(msg);
            judgerResult.setRuntimeResult(runTimeResult.getRes());
            rabbitTemplate.convertAndSend(MsgConst.EXCHANGE, MsgConst.ROUTKEY, judgerResult);
            throw new NoPassException();
        }
        try {
            if (!comparator.isOutputTheSame(outputSamp, output)) {
                JudgerResult judgerResult = new JudgerResult();
                judgerResult.setSubmission(msg);
                judgerResult.setError("checkpoint error");
                ByteArrayOutputStream outputSmp = new ByteArrayOutputStream();
                ByteArrayOutputStream outputusr = new ByteArrayOutputStream();
                IOUtil.copy(outputSmp, new File(outputSamp));
                IOUtil.copy(outputusr, new File(output));
                judgerResult.setOutputSample(new String(outputSmp.toByteArray()));
                judgerResult.setOutput(new String(outputusr.toByteArray()));
                judgerResult.setRuntimeResult(runTimeResult.getRes());
                judgerResult.setUseTime(runTimeResult.getUsedTime());
                judgerResult.setUseTime(runTimeResult.getUsedMemory());
                rabbitTemplate.convertAndSend(MsgConst.EXCHANGE, MsgConst.ROUTKEY, judgerResult);
                throw new NoPassException();
            }
        } catch (IOException e) {
            throw new IllegalStateException("在校验用户输出时错误", e);
        }
    }

    /**
     * 完成所有测试点
     *
     * @param msg
     * @param useTime
     * @param userMem
     */
    private void onAllCheckPointFinish(SubmissionMsg msg, int useTime, int userMem) {
        log.info("测试点全部通过，problemid[{}]-submission[{}]", msg.getProblemId(), msg.getSubmissionId());
        JudgerResult judgerResult = new JudgerResult();
        judgerResult.setUseTime(useTime);
        judgerResult.setUserMem(userMem);
        judgerResult.setSubmission(msg);
        judgerResult.setRuntimeResult(RuntimeResult.PS);
        rabbitTemplate.convertAndSend(MsgConst.EXCHANGE, MsgConst.ROUTKEY, judgerResult);
    }


}
