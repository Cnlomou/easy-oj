package org.example.judger;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Cnlomou
 * @create 2020/7/5 14:54
 */
@SpringBootApplication
@MapperScan(basePackages = "org.example.mapper")
public class JudApplication {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JudApplication.class, args);
        configRabbit(run);
    }

    public static void configRabbit(ApplicationContext applicationContext){
        RabbitTemplate bean = applicationContext.getBean(RabbitTemplate.class);
        if(bean!=null){
            bean.setMessageConverter(new Jackson2JsonMessageConverter());
        }
    }
}
