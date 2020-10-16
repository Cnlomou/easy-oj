package org.example.judger.core;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.example.bean.RuntimeResult;
import org.example.judger.util.NativeLibLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/7/11 13:46
 *
 * 用于执行任务
 */
@Component
public class Runner {


    /**
     * 获取程序运行结果.
     * @param commandLine - 待执行程序的命令行
     * @param systemUsername - 登录操作系统的用户名
     * @param systemPassword - 登录操作系统的密码
     * @param inputFilePath - 输入文件路径(可为NULL)
     * @param outputFilePath - 输出文件路径(可为NULL)
     * @param timeLimit - 时间限制(单位ms, 0表示不限制)
     * @param memoryLimit - 内存限制(单位KB, 0表示不限制)
     * @return 一个包含程序运行结果的Map<String, Object>对象
     */
    public native Map<String, Object> getRuntimeResult(String commandLine,
                                                       String systemUsername, String systemPassword, String inputFilePath,
                                                       String outputFilePath, int timeLimit, int memoryLimit);

    /**
     * 将结果集转化成对象
     * @param map 结果集
     * @return RunTimeResult对象
     */
    public RunTimeResult transForm(Map<String,Object> map,int timeLimit,int memoryLimit){
        String string = JSON.toJSONString(map);
        RunTimeResult runTimeResult = JSON.parseObject(string, RunTimeResult.class);
        getRuntimeResultTag(runTimeResult,timeLimit,memoryLimit);
        return runTimeResult;
    }

    private String getRuntimeResultTag(RunTimeResult runTimeResult,int timeLimit, int memoryLimit){
        if ( runTimeResult.exitCode == 0 ) {
            runTimeResult.setRes(RuntimeResult.AC);
            //运行成功
            return "AC";
        }
        if ( runTimeResult.usedTime >= timeLimit ) {
            runTimeResult.setRes(RuntimeResult.TLE);
            return "TLE";
        }
        if ( runTimeResult.usedMemory >= memoryLimit ) {
            runTimeResult.setRes(RuntimeResult.MLE);
            return "MLE";
        }
        runTimeResult.setRes(RuntimeResult.RE);
        //发生错误
        return "RE";
    }
    /**
     * 接收运行结果的对象
     */
    @Data
    public static class RunTimeResult{
        private int usedTime;
        private int usedMemory;
        private int exitCode;
        private RuntimeResult res;
    }
    static {
        //System.loadLibrary("E:\\ideaProject\\ndoj\\judger\\src\\main\\resources\\JudgerCore.dll");
        try {
            NativeLibLoader.loadLibrary("JUDCORE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
