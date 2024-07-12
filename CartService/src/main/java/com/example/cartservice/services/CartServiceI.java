package com.example.cartservice.services;

import com.example.cartservice.dtos.AddToCartRequestDTO;
import com.example.cartservice.dtos.CartResponseDTO;
import com.example.cartservice.dtos.CheckoutRequestDTO;
import com.example.cartservice.dtos.RemoveFromCartRequestDTO;

public interface CartServiceI {
    CartResponseDTO addToCart(AddToCartRequestDTO request);
    CartResponseDTO reviewCart(Long userId);
    String checkout(CheckoutRequestDTO request);
    CartResponseDTO removeFromCart(RemoveFromCartRequestDTO request);
    void deleteCart(Long userId);
}
