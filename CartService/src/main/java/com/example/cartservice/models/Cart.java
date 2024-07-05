package com.example.cartservice.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private double totalPrice;
    private String address;
    private String paymentMethod;

    public Cart() {}

    public Cart(Long userId) {
        this.userId = userId;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }
}
