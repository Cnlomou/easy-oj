package org.example.web.message;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.bean.JudgerResult;
import org.example.bean.SubmissionMsg;
import org.example.web.service.SubmissionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Cnlomou
 * @create 2020/8/12 15:53
 */

@Component
@Slf4j
public class SubmissionListener {

    @Autowired
    private SubmissionService submissionService;
    @RabbitListener(queues = "oj.submission.result")
    public void onJudgerResult(String msg){
//        System.out.println(msg);
        submissionService.onJudgerResult(JSON.parseObject(msg,JudgerResult.class));
    }
}
