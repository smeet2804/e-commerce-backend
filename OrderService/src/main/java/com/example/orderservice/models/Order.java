package com.example.orderservice.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private Long userId;
    private List<OrderItem> items;
    private String address;
    private String paymentMethod;
    private OrderStatus status;
    private double totalAmount;
}
