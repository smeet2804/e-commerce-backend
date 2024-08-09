package com.example.orderservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                String token = (String) requestAttributes.getAttribute("Authorization", RequestAttributes.SCOPE_REQUEST);

                if (token != null) {
                    requestTemplate.header("Authorization", "Bearer " + token);
                }

            }
        };
    }
}
