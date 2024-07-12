package com.example.cartservice.mappers;

import com.example.cartservice.dtos.CartItemResponseDTO;
import com.example.cartservice.dtos.CartResponseDTO;
import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDTO toCartResponse(Cart cart) {
        CartResponseDTO response = new CartResponseDTO();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setTotalPrice(cart.getTotalPrice());
        response.setAddress(cart.getAddress());
        response.setPaymentMethod(cart.getPaymentMethod());

        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(CartMapper::toCartItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }

    public static CartItemResponseDTO toCartItemResponse(CartItem item) {
        CartItemResponseDTO itemResponse = new CartItemResponseDTO();
        itemResponse.setId(item.getId());
        itemResponse.setProductId(item.getProductId());
        itemResponse.setQuantity(item.getQuantity());
        itemResponse.setPrice(item.getPrice());
        itemResponse.setProductName(item.getProductName());
        itemResponse.setProductDescription(item.getProductDescription());

        return itemResponse;
    }
}
