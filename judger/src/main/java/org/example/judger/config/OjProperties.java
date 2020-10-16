package org.example.judger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Cnlomou
 * @create 2020/7/11 15:01
 */
@Data
public class OjProperties {
    private String workPath;
    private String userName;
    private String passWord;
}
