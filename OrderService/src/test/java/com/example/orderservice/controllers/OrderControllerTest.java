package com.example.orderservice.controllers;

import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.OrderResponseDTO;
import com.example.orderservice.dtos.OrderStatusDTO;
import com.example.orderservice.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createOrder_WhenValidRequest_ReturnsOrderId() throws Exception {
        // Arrange
        CartDTO cartDTO = new CartDTO();
        // set up cartDTO fields...

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
        // set up other fields...

        when(orderService.createOrder(any(CartDTO.class))).thenReturn(orderResponseDTO);

        // Act and Assert
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer valid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // Verify
        verify(orderService).createOrder(any(CartDTO.class));
    }

    @Test
    public void createOrder_WhenUnauthorized_ReturnsStatusUnauthorized() throws Exception {
        // Arrange
        CartDTO cartDTO = new CartDTO();
        // set up cartDTO fields...

        // Act and Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void viewOrder_WhenOrderExists_ReturnsOrder() throws Exception {
        // Arrange
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
        // set up other fields...

        when(orderService.getOrderById(1L)).thenReturn(orderResponseDTO);

        // Act and Assert
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderResponseDTO)));

        // Verify
        verify(orderService).getOrderById(1L);
    }

    @Test
    public void viewOrder_WhenOrderNotExists_ReturnsNotFound() throws Exception {
        // Arrange
        when(orderService.getOrderById(1L)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound());

        // Verify
        verify(orderService).getOrderById(1L);
    }

    @Test
    public void viewOrdersByUser_WhenCalled_ReturnsListOfOrders() throws Exception {
        // Arrange
        List<OrderResponseDTO> orderList = new ArrayList<>();
        OrderResponseDTO order1 = new OrderResponseDTO();
        order1.setId(1L);
        // set up other fields...

        OrderResponseDTO order2 = new OrderResponseDTO();
        order2.setId(2L);
        // set up other fields...

        orderList.add(order1);
        orderList.add(order2);

        when(orderService.getOrdersByUserId(1L)).thenReturn(orderList);

        // Act and Assert
        mockMvc.perform(get("/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderList)));

        // Verify
        verify(orderService).getOrdersByUserId(1L);
    }

    @Test
    public void updateOrderStatus_WhenOrderExists_ReturnsUpdatedOrder() throws Exception {
        // Arrange
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatus("SHIPPED");

        OrderResponseDTO updatedOrder = new OrderResponseDTO();
        updatedOrder.setId(1L);
        updatedOrder.setStatus("SHIPPED");
        // set up other fields...

        when(orderService.updateOrderStatus(1L, "SHIPPED")).thenReturn(updatedOrder);

        // Act and Assert
        mockMvc.perform(put("/orders/status/1")
                        .header("Authorization", "Bearer valid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedOrder)));

        // Verify
        verify(orderService).updateOrderStatus(1L, "SHIPPED");
    }

    @Test
    public void updateOrderStatus_WhenUnauthorized_ReturnsStatusUnauthorized() throws Exception {
        // Arrange
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatus("SHIPPED");

        // Act and Assert
        mockMvc.perform(put("/orders/status/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteOrder_WhenOrderExists_ReturnsStatusNoContent() throws Exception {
        // Act and Assert
        mockMvc.perform(delete("/orders/1")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isNoContent());

        // Verify
        verify(orderService).deleteOrder(1L);
    }

}
