package com.example.productcatalogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPriceDTO {
    private Long productId;
    private double price;
}
