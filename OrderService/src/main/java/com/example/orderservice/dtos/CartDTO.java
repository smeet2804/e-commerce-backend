package com.example.orderservice.dtos;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartDTO {

    private Long id;
    private Long userId;
    private List<CartItemDTO> items = new ArrayList<>();
    private double totalPrice;
    private String address;
    private String paymentMethod;

}
