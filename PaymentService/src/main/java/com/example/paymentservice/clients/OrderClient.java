package com.example.paymentservice.clients;

import com.example.paymentservice.dtos.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "orderservice")
public interface OrderClient {

    @GetMapping("/orders/{orderId}")
    OrderDTO getOrderById(@PathVariable("orderId") String orderId);

    @PutMapping("/orders/{orderId}/status")
    void updateOrderStatus(@PathVariable("orderId") String orderId, @RequestParam String status);
}