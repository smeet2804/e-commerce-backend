package com.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDTO {
    private Long userId;
    private String address;
    private String paymentMethod;
}