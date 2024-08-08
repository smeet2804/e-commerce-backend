package com.example.orderservice.controllers;

import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.OrderRequestDTO;
import com.example.orderservice.dtos.OrderResponseDTO;
import com.example.orderservice.dtos.OrderStatusDTO;
import com.example.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody CartDTO cartDTO) {
        OrderResponseDTO orderDTO = orderService.createOrder(cartDTO);
        return ResponseEntity.ok(orderDTO.getId());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> viewOrder(@PathVariable Long orderId) {
        OrderResponseDTO orderDTO = orderService.getOrderById(orderId);
        return orderDTO != null ? ResponseEntity.ok(orderDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> viewOrdersByUser(@PathVariable Long userId) {
        List<OrderResponseDTO> orderDTOs = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orderDTOs);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusDTO statusDTO) {
        OrderResponseDTO orderDTO = orderService.updateOrderStatus(orderId, statusDTO.getStatus());
        return orderDTO != null ? ResponseEntity.ok(orderDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
