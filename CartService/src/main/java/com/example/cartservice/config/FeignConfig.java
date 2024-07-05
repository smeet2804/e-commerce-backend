package com.example.cartservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = "your_bearer_token";
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
