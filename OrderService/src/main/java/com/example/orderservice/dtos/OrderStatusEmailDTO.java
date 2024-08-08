package com.example.orderservice.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusEmailDTO {
    private String to;
    private String subject;
    private String text;

}
