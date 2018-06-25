package com.qin.springboot.a002;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "c3p0")
@PropertySource({"classpath:c3p0config.properties"})
@Data
public class MyWebConfig {
    private String jdbcUrl;
}
