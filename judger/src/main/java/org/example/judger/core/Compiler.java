package org.example.judger.core;

import org.example.bean.SubmissionMsg;
import org.example.judger.config.OjProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.logging.FileHandler;

/**
 * @author Cnlomou
 * @create 2020/7/11 13:45
 *
 * 用于代码的编译
 */
@Component
public class Compiler {
    /**
     * 编译源代码
     * @param srcPath 文件名
     * @param msg
     * @param runtime
     */
    public Runner.RunTimeResult compile(String srcPath, SubmissionMsg msg, Runner runtime, OjProperties ojProperties) {

        String languageCompileCommand = msg.getLanguage().getLanguageCompileCommand();
        String substring = srcPath.substring(0, srcPath.lastIndexOf('.'));
        String command = languageCompileCommand.replace("{filename}", substring);
        String compileLog=ojProperties.getWorkPath()+"/compileLog.txt";

        //执行
        Map<String, Object> runtimeResult = runtime.getRuntimeResult(command, ojProperties.getUserName(),
                ojProperties.getPassWord(), null, compileLog, 10000, 10000);

        Runner.RunTimeResult runTimeResult = runtime.transForm(runtimeResult,1000,1000);

        return runTimeResult;
    }
}
