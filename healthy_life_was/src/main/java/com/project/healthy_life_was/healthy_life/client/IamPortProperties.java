package com.project.healthy_life_was.healthy_life.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "iamport")
@Data
public class IamPortProperties {
    private String code;
    private String restKey;
    private String restSecret;
    private String apiBase;

}