package org.example.judger.message;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.bean.SubmissionMsg;
import org.example.judger.core.Dispatcher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Cnlomou
 * @create 2020/7/11 14:04
 *
 * 监听消息队列
 */
@Component
@Slf4j
public class SubmissionListener {

    @Autowired
    private Dispatcher taskDispatcher;

    /**
     * 监听submission创建事件队列
     * @param msg
     */
    @RabbitListener(queues = "oj.submission.created")
    public void onMessage(String msg){
        SubmissionMsg submissionMsg = JSON.parseObject(msg, SubmissionMsg.class);
        taskDispatcher.onSubmissionCreated(submissionMsg);
    }
}
