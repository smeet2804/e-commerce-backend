package com.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
    private Long productId;
    private String productDescription;
}
