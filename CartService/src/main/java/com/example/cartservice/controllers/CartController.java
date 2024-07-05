package com.example.cartservice.controllers;


import com.example.cartservice.models.Cart;
import com.example.cartservice.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        Cart updatedCart = cartService.addToCart(userId, productId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/review/{userId}")
    public ResponseEntity<Cart> reviewCart(@PathVariable Long userId) {
        Cart cart = cartService.reviewCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> checkout(@PathVariable Long userId, @RequestParam String address, @RequestParam String paymentMethod) {
        String result = cartService.checkout(userId, address, paymentMethod);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        Cart updatedCart = cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.ok("Cart deleted successfully");
    }
}
