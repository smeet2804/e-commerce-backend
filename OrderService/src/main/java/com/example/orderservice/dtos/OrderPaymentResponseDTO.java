package com.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPaymentResponseDTO {
    private Long orderId;
    private Double price;
    private Long userId;
    private String address;
    private String status;
}
