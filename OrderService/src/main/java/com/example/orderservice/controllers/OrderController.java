package com.example.orderservice.controllers;

import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.OrderResponseDTO;
import com.example.orderservice.dtos.OrderStatusDTO;
import com.example.orderservice.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody CartDTO cartDTO) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            requestAttributes.setAttribute("Authorization", token, RequestAttributes.SCOPE_REQUEST);

            OrderResponseDTO orderDTO = orderService.createOrder(cartDTO);
            return ResponseEntity.ok(orderDTO.getId());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

    @PutMapping("/status/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusDTO statusDTO) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            requestAttributes.setAttribute("Authorization", token, RequestAttributes.SCOPE_REQUEST);

            OrderResponseDTO orderDTO = orderService.updateOrderStatus(orderId, statusDTO.getStatus());
            return orderDTO != null ? ResponseEntity.ok(orderDTO) : ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
