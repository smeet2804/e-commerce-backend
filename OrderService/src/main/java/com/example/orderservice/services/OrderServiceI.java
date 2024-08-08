package com.example.orderservice.services;

import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.OrderResponseDTO;

import java.util.List;

public interface OrderServiceI {
    OrderResponseDTO createOrder(CartDTO cartDTO);
    OrderResponseDTO getOrderById(Long orderId);
    List<OrderResponseDTO> getOrdersByUserId(Long userId);
    OrderResponseDTO updateOrderStatus(Long orderId, String status);
    void deleteOrder(Long orderId);
}
