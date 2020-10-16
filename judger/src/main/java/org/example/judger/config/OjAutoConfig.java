package org.example.judger.config;

import org.example.model.OjLanguages;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Cnlomou
 * @create 2020/7/11 15:02
 */
@Configuration
public class OjAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "oj")
    public OjProperties ojproperties(){
        return new OjProperties();
    }
}
