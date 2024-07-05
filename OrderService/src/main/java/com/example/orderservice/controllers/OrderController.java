package com.example.orderservice.controllers;

import com.example.orderservice.models.Order;
import com.example.orderservice.models.Cart;
import com.example.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public String createOrder(@RequestBody Cart cart) {
        Order order = orderService.createOrder(cart);
        return order.getId();
    }
}

