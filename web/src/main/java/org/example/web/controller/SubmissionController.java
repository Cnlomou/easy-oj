package org.example.web.controller;

import org.example.bean.JudgerResult;
import org.example.web.entity.Result;
import org.example.web.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/8/7 15:42
 */

@Controller
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;
    @PostMapping("/ques/{id}")
    @ResponseBody
    public Result<?> runnerCode(@PathVariable(name = "id")Integer id, String code, String type){
        Integer subId=0;
        if(StringUtil.isEmpty(type)){
            return Result.error("不能识别的代码");
        }else if(StringUtil.isEmpty(code))
            return Result.error("填写代码后提交");
        else
            subId=submissionService.addToMessageQueue(id,code,type);
        return new Result<>(true,200,subId,"success");
    }

    @PostMapping("/res/{id}")
    @ResponseBody
    public Result<Map<String,String>> judgerResult(@PathVariable(name = "id")Integer subId){
        JudgerResult judgerResult = submissionService.getJudgerResult(subId);
        HashMap<String, String> map = new HashMap<>();
        Result<Map<String,String>> objectResult = new Result<>(true,200,map,"ok");
        map.put("useTm",Integer.toString(judgerResult.getUseTime()));
        map.put("useMm",Integer.toString(judgerResult.getUserMem()));
        map.put("res",judgerResult.getRuntimeResult().toString());
        map.put("out",judgerResult.getOutput());
        map.put("outSam",judgerResult.getOutputSample());
        map.put("msg",judgerResult.getError());
        return objectResult;
    }
}
