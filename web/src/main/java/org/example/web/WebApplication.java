package org.example.web;

import org.example.web.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Cnlomou
 * @create 2020/7/5 13:52
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.example.mapper"})
//@EnableAsync(proxyTargetClass = true)
public class WebApplication {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(WebApplication.class, args);
        //对rabbit 配置
        configRabbit(applicationContext);
    }

    public static void configRabbit(ApplicationContext applicationContext){
        RabbitTemplate bean = applicationContext.getBean(RabbitTemplate.class);
        if(bean!=null){
            bean.setMessageConverter(new Jackson2JsonMessageConverter());
        }
    }
}
