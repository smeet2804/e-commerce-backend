package com.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPriceDTO {
    private String productId;
    private double price;
}
