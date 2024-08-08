package com.example.orderservice.clients;

import com.example.orderservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/users/{userId}")
    String getUserDetails(@PathVariable Long userId);
}
