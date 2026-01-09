package com.project.healthy_life_was.healthy_life.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient iamportWebClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.iamport.kr").build();
    }
}