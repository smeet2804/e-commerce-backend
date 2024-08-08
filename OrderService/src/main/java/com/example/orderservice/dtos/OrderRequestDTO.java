package com.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private Long id;
    private Long userId;
    private List<OrderItemDTO> items;
    private String address;
    private String paymentMethod;
    private String status;
    private double totalAmount;
}
