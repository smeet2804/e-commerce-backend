package com.example.orderservice.services;

import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderItem;
import com.example.orderservice.models.OrderStatus;
import com.example.orderservice.models.Cart;
import com.example.orderservice.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Cart cart) {

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setProductName(cartItem.getProductName());
                    orderItem.setProductDescription(cartItem.getProductDescription());
                    return orderItem;
                })
                .collect(Collectors.toList());


        Order order = new Order();
        order.setUserId(cart.getUserId());
        order.setItems(orderItems);
        order.setAddress(cart.getAddress());
        order.setPaymentMethod(cart.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(cart.getTotalPrice());

        return orderRepository.save(order);
    }
}