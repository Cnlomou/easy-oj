package org.example.judger;

import org.example.bean.SubmissionMsg;
import org.example.judger.JudApplication;
import org.example.judger.core.Runner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/7/11 17:10
 */

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = JudApplication.class)
public class JudApplicationTest {

    @Resource
    Runner runner;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void test(){
        Runner runner = new Runner();
        Map<String, Object> runtimeResult = runner.getRuntimeResult("mspaint", "1246269795@qq.com", "sjh20000903", null,
                "D:\\out.txt", 100000, 100000);
        System.out.println(runtimeResult);
    }

    //send message
    @Test
    public void sendMessage(){
        SubmissionMsg object = new SubmissionMsg();
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        for(int i=0;i<3;i++)
            rabbitTemplate.convertAndSend("oj.submission","oj.submission.created", object);
    }
}
