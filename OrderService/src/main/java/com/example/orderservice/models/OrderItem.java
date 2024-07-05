package com.example.orderservice.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private Long productId;
    private int quantity;
    private double price;
    private String productName;
    private String productDescription;

}