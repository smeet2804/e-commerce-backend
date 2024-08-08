package com.example.orderservice.services;

import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.OrderRequestDTO;
import com.example.orderservice.dtos.OrderResponseDTO;
import com.example.orderservice.mappers.OrderMapper;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderStatus;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceI {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderResponseDTO createOrder(CartDTO cartDTO) {
        Order order = new Order();
        // Set order properties from cartDTO
        orderRepository.save(order);
        return OrderMapper.getOrderResponseDTOFromOrder(order);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return order != null ? OrderMapper.getOrderResponseDTOFromOrder(order) : null;
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderMapper::getOrderResponseDTOFromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.valueOf(status));
            orderRepository.save(order);
        }
        return order != null ? OrderMapper.getOrderResponseDTOFromOrder(order) : null;
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
