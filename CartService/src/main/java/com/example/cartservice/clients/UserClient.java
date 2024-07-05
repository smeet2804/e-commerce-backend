package com.example.cartservice.clients;

import com.example.cartservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice", configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/user/{userId}")
    String getUserDetails(@PathVariable Long userId);
}
