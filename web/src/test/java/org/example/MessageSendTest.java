package org.example;

import org.example.bean.SubmissionMsg;
import org.example.web.WebApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Cnlomou
 * @create 2020/8/2 15:03
 */
@SpringBootTest(classes = WebApplication.class)
public class MessageSendTest {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void msgSender(){
        SubmissionMsg object = new SubmissionMsg();
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        for(int i=0;i<3;i++)
            rabbitTemplate.convertAndSend("oj.submission","submission.event.created", object);
    }

}
