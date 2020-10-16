package org.example.web.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.bean.JudgerResult;
import org.example.bean.SubmissionMsg;
import org.example.mapper.OjLanguagesMapper;
import org.example.mapper.OjSubmissionsMapper;
import org.example.model.OjLanguages;
import org.example.model.OjSubmissions;
import org.example.web.message.JudgerResultManager;
import org.example.web.service.SubmissionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/8/7 15:38
 */

/**
 * 将上传的代码放去消息队列中
 */
@Service
@Slf4j
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private OjLanguagesMapper ojLanguagesMapper;
    @Autowired
    private OjSubmissionsMapper ojSubmissionsMapper;

    @Autowired
    private JudgerResultManager judgerResultManager;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 将用户提交的代码，放入消息队列
     * @param id
     * @param code
     * @param type
     * @return
     */
    @Override
    public Integer addToMessageQueue(Integer id, String code, String type) {
        SubmissionMsg msg = new SubmissionMsg();
        OjLanguages languageByName = getLanguageByName(type);
        if(languageByName==null){
            log.warn("不支持的代码：{}",type);
            throw new IllegalStateException("不支持"+type+"的代码");
        }
        OjSubmissions submission = createSubmission(code, languageByName.getLanguageId(), id);
        if(log.isInfoEnabled())
            log.info("创建一个新的submission-{}",submission.getSubmissionId());
        msg.setSubmissionId(submission.getSubmissionId());
        msg.setLanguage(languageByName);
        msg.setCode(code);
        msg.setProblemId(id);
        //放入消息队列
        rabbitTemplate.convertAndSend("oj.submission","submission.event.created", msg);
        return submission.getSubmissionId();
    }

    /**
     * 评测结果的回调方法
     * @param judgerResult
     */
    @Override
    public void onJudgerResult(JudgerResult judgerResult) {
        int subId=judgerResult.getSubmission().getSubmissionId();
        updateData(subId,judgerResult);
        judgerResultManager.add(subId,judgerResult);
    }

    @Override
    public JudgerResult getJudgerResult(Integer subId) {
        judgerResultManager.wait(subId);
        JudgerResult result = judgerResultManager.getResult(subId);
        judgerResultManager.del(subId);
        return result;
    }

    /**
     * 更新数据库的数据
     * @param subId
     * @param judgerResult
     */
    private void updateData(int subId, JudgerResult judgerResult) {
        OjSubmissions ojSubmissions = new OjSubmissions();
        ojSubmissions.setSubmissionId(subId);
        ojSubmissions.setSubmissionLog(judgerResult.getOutput());
        ojSubmissions.setSubmissionExeTime(new Date());
        ojSubmissions.setSubmissionCode(judgerResult.getSubmission().getCode());
        ojSubmissions.setSubmissionResult(judgerResult.getRuntimeResult().toString());
        if(StringUtil.isEmpty(judgerResult.getError())){
            ojSubmissions.setSubmissionUseMemory(judgerResult.getUserMem()/10);
            ojSubmissions.setSubmissionUseTime(judgerResult.getUseTime()/10);
        }
        ojSubmissionsMapper.updateByPrimaryKeySelective(ojSubmissions);
    }

    private OjSubmissions createSubmission(String code, Integer languageId, Integer problemId) {
        OjSubmissions ojSubmissions = new OjSubmissions();
        ojSubmissions.setProblemId(problemId);
        ojSubmissions.setLanguageId(languageId);
        Date date = new Date();
        ojSubmissions.setSubmissionCommitTime(date);
        ojSubmissions.setSubmissionExeTime(date);
        ojSubmissions.setSubmissionUseTime(-1);
        ojSubmissions.setSubmissionUseMemory(-1);
        ojSubmissions.setSubmissionResult("PD");//等待中
        ojSubmissions.setSubmissionScore(-1);
        ojSubmissions.setSubmissionLog("");
        ojSubmissions.setSubmissionCode(code);
        ojSubmissionsMapper.insertSelective(ojSubmissions);
        return ojSubmissions;
    }

    private OjLanguages getLanguageByName(String type) {
        Example example = Example.builder(OjLanguages.class)
                .andWhere(Sqls.custom().andEqualTo("languageName", type))
                .build();
        List<OjLanguages> ojLanguages = ojLanguagesMapper.selectByExample(example);
        if(ojLanguages==null||ojLanguages.size()==0)
            return null;
        return ojLanguages.get(0);
    }
}
