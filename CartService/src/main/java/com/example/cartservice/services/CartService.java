package com.example.cartservice.services;

import com.example.cartservice.clients.OrderClient;
import com.example.cartservice.clients.ProductClient;
import com.example.cartservice.clients.UserClient;
import com.example.cartservice.dtos.*;
import com.example.cartservice.mappers.CartMapper;
import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;
import com.example.cartservice.models.Product;
import com.example.cartservice.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements CartServiceI{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private OrderClient orderClient;

    public CartResponseDTO addToCart(AddToCartRequestDTO request) {
        Cart cart = cartRepository.findByUserId(request.getUserId()).orElse(new Cart(request.getUserId()));
        double totalPrice = cart.getTotalPrice();
        CartItem cartItem = new CartItem();
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        ProductPriceDTO productPriceDTO = productClient.getProductPrice(request.getProductId());
        double productPrice = productPriceDTO.getPrice();
        cartItem.setPrice(request.getQuantity() * productPrice);
        totalPrice += request.getQuantity() * productPrice;
        cart.addItem(cartItem);
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        return CartMapper.toCartResponse(cart);
    }

    public CartResponseDTO reviewCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart(userId));
        cart.getItems().forEach(item -> {
            Product product = productClient.getProductDetails(item.getProductId());
            item.setProductName(product.getName());
            item.setProductDescription(product.getDescription());
            item.setPrice(item.getQuantity() * product.getPrice());
        });
        return CartMapper.toCartResponse(cart);
    }

    public String checkout(CheckoutRequestDTO request) {
        Cart cart = cartRepository.findByUserId(request.getUserId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setAddress(request.getAddress());
        cart.setPaymentMethod(request.getPaymentMethod());
        String orderResponse = orderClient.createOrder(cart);
        cartRepository.delete(cart);
        return "Order successfully created with orderId: " + orderResponse;
    }

    public CartResponseDTO removeFromCart(RemoveFromCartRequestDTO request) {
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Optional<CartItem> cartItemOptional = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            int quantity = cartItem.getQuantity();

            ProductPriceDTO productPriceDTO = productClient.getProductPrice(request.getProductId());
            double productPrice = productPriceDTO.getPrice();

            cart.removeItem(request.getProductId());
            double totalPrice = cart.getTotalPrice() - (quantity * productPrice);
            cart.setTotalPrice(totalPrice);
        }

        cartRepository.save(cart);
        return CartMapper.toCartResponse(cart);
    }

    public void deleteCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }
}
