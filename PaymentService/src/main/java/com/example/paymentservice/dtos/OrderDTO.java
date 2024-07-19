package com.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private String orderId;
    private Long price;
    private Long userId;
    private String address;
    private String status;
}
