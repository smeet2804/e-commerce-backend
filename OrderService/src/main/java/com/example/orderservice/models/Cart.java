package com.example.orderservice.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart {

    private Long id;
    private Long userId;
    private List<CartItem> items = new ArrayList<>();
    private double totalPrice;
    private String address;
    private String paymentMethod;

}
