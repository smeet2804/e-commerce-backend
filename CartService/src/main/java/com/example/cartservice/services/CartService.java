package com.example.cartservice.services;

import com.example.cartservice.clients.OrderClient;
import com.example.cartservice.clients.ProductClient;
import com.example.cartservice.clients.UserClient;
import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;
import com.example.cartservice.models.Product;
import com.example.cartservice.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderClient orderClient;

    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        double totalPrice = cart.getTotalPrice();
        CartItem cartItem = new CartItem(productId, quantity, quantity * productClient.getProductPrice(productId));
        totalPrice += quantity * productClient.getProductPrice(productId);
        cart.addItem(cartItem);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    public Cart reviewCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        cart.getItems().forEach(item -> {
            Product product = productClient.getProductDetails(item.getProductId());
            item.setProductName(product.getName());
            item.setProductDescription(product.getDescription());
            item.setPrice(item.getQuantity() * product.getPrice());
        });
        return cart;
    }

    public String checkout(Long userId, String address, String paymentMethod) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        // Implement the checkout logic, interact with payment service etc.
        cart.setAddress(address);
        cart.setPaymentMethod(paymentMethod);
        String orderResponse = orderClient.createOrder(cart);
        cartRepository.delete(cart);
        return "Order successfully created with orderId: " + orderResponse;
    }

    public Cart removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (cart.getItems().stream().anyMatch(item -> item.getProductId().equals(productId))) {
            cart.removeItem(productId);
            cart.setTotalPrice(cart.getTotalPrice() - productClient.getProductPrice(productId));
        }
        return cartRepository.save(cart);
    }

    public void deleteCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }
}
