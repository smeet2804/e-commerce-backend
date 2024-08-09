package com.example.paymentservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            System.out.println("Request attributes: "+requestAttributes);
            if (requestAttributes != null) {
                String token = (String) requestAttributes.getAttribute("Authorization", RequestAttributes.SCOPE_REQUEST);
                System.out.println("Token: " + token);
                if (token != null) {
                    requestTemplate.header("Authorization", "Bearer " + token);
                }
            }
        };
    }
}
