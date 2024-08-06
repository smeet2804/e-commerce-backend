package com.example.cartservice.clients;

import com.example.cartservice.config.FeignConfig;
import com.example.cartservice.models.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "OrderService", configuration = FeignConfig.class)
public interface OrderClient {
    @PostMapping("/orders")
    String createOrder(@RequestBody Cart cart);
}
