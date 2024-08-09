package com.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private Long orderId;
    private Double price;
    private Long userId;
    private String address;
    private String status;
}
