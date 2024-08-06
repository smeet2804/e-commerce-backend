package com.example.cartservice.controllers;

import com.example.cartservice.dtos.*;
import com.example.cartservice.services.CartServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartServiceI cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    public void addToCart_WhenCalled_ReturnsCartResponse() throws Exception {
        // Arrange
        AddToCartRequestDTO request = new AddToCartRequestDTO();
        request.setUserId(1L);
        request.setProductId(1L);
        request.setQuantity(2);

        CartItemResponseDTO item = new CartItemResponseDTO();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(20.0);
        item.setProductName("Product 123");
        item.setProductDescription("Description for product 123");

        CartResponseDTO response = new CartResponseDTO();
        response.setUserId(1L);
        response.setItems(List.of(item));
        response.setTotalPrice(40.0);

        when(cartService.addToCart(any(AddToCartRequestDTO.class))).thenReturn(response);

        // Act and Assert
        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        // Verify
        verify(cartService).addToCart(any(AddToCartRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void reviewCart_WhenCalled_ReturnsCartResponse() throws Exception {
        // Arrange
        Long userId = 1L;

        CartItemResponseDTO item1 = new CartItemResponseDTO();
        item1.setProductId(1L);
        item1.setQuantity(2);
        item1.setPrice(20.0);
        item1.setProductName("Product 123");
        item1.setProductDescription("Description for product 123");

        CartItemResponseDTO item2 = new CartItemResponseDTO();
        item2.setProductId(2L);
        item2.setQuantity(1);
        item2.setPrice(15.0);
        item2.setProductName("Product 456");
        item2.setProductDescription("Description for product 456");

        CartResponseDTO response = new CartResponseDTO();
        response.setUserId(1L);
        response.setItems(List.of(item1, item2));
        response.setTotalPrice(55.0);

        when(cartService.reviewCart(userId)).thenReturn(response);

        // Act and Assert
        mockMvc.perform(get("/cart/review/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        // Verify
        verify(cartService).reviewCart(userId);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void checkout_WhenCalled_ReturnsStringResponse() throws Exception {
        // Arrange
        CheckoutRequestDTO request = new CheckoutRequestDTO();
        request.setUserId(1L);
        request.setPaymentMethod("Credit Card");
        request.setAddress("123 Main St, Springfield");

        String result = "Checkout successful";

        when(cartService.checkout(any(CheckoutRequestDTO.class))).thenReturn(result);

        // Act and Assert
        mockMvc.perform(post("/cart/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(result));

        // Verify
        verify(cartService).checkout(any(CheckoutRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void removeFromCart_WhenCalled_ReturnsCartResponse() throws Exception {
        // Arrange
        RemoveFromCartRequestDTO request = new RemoveFromCartRequestDTO();
        request.setUserId(1L);
        request.setProductId(1L);

        CartItemResponseDTO item = new CartItemResponseDTO();
        item.setProductId(2L);
        item.setQuantity(1);
        item.setPrice(15.0);
        item.setProductName("Product 456");
        item.setProductDescription("Description for product 456");

        CartResponseDTO response = new CartResponseDTO();
        response.setUserId(1L);
        response.setItems(List.of(item));
        response.setTotalPrice(15.0);

        when(cartService.removeFromCart(any(RemoveFromCartRequestDTO.class))).thenReturn(response);

        // Act and Assert
        mockMvc.perform(delete("/cart/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        // Verify
        verify(cartService).removeFromCart(any(RemoveFromCartRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteCart_WhenCalled_ReturnsStringResponse() throws Exception {
        // Arrange
        Long userId = 1L;

        // Act and Assert
        mockMvc.perform(delete("/cart/delete/{userId}", userId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart deleted successfully"));

        // Verify
        verify(cartService).deleteCart(userId);
    }
}
