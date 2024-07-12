package com.example.cartservice.controllers;

import com.example.cartservice.dtos.*;
import com.example.cartservice.services.CartServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartServiceI cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody AddToCartRequestDTO request) {
        CartResponseDTO response = cartService.addToCart(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/review/{userId}")
    public ResponseEntity<CartResponseDTO> reviewCart(@PathVariable Long userId) {
        CartResponseDTO response = cartService.reviewCart(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequestDTO request) {
        String result = cartService.checkout(request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponseDTO> removeFromCart(@RequestBody RemoveFromCartRequestDTO request) {
        CartResponseDTO response = cartService.removeFromCart(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.ok("Cart deleted successfully");
    }
}
