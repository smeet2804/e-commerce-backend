package com.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private double price;
    private String productName;
    private String productDescription;
}