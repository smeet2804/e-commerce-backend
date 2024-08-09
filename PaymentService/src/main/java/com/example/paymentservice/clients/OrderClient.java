package com.example.paymentservice.clients;

import com.example.paymentservice.config.FeignConfig;
import com.example.paymentservice.dtos.OrderDTO;
import com.example.paymentservice.models.OrderStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "OrderService", configuration = FeignConfig.class)
public interface OrderClient {

    @GetMapping("/orders/payment/{orderId}")
    OrderDTO getOrderDetailsForPayment(@PathVariable("orderId") Long orderId);

    @PutMapping("/orders/status/{orderId}")
    void updateOrderStatus(@PathVariable("orderId") Long orderId, @RequestBody OrderStatusDTO orderStatusDTO);
}