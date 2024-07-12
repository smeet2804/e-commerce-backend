package com.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequestDTO {
    private Long userId;
    private String productId;
    private int quantity;
}