package com.example.orderservice.mappers;

import com.example.orderservice.dtos.OrderItemDTO;
import com.example.orderservice.dtos.OrderPaymentResponseDTO;
import com.example.orderservice.dtos.OrderRequestDTO;
import com.example.orderservice.dtos.OrderResponseDTO;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderItem;
import com.example.orderservice.models.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order getOrderFromRequestDTO(OrderRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setId(dto.getId());
        order.setUserId(dto.getUserId());
        order.setItems(dto.getItems().stream().map(OrderMapper::getOrderItemFromDTO).collect(Collectors.toList()));
        order.setAddress(dto.getAddress());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        order.setTotalAmount(dto.getTotalAmount());
        return order;
    }

    public static OrderResponseDTO getOrderResponseDTOFromOrder(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setItems(order.getItems().stream().map(OrderMapper::getOrderItemDTOFromOrderItem).collect(Collectors.toList()));
        dto.setAddress(order.getAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        return dto;
    }

    public static List<OrderResponseDTO> getOrderResponseDTOListFromOrders(List<Order> orders) {
        List<OrderResponseDTO> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(getOrderResponseDTOFromOrder(order));
        }
        return dtos;
    }

    private static OrderItem getOrderItemFromDTO(OrderItemDTO dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setProductName(dto.getProductName());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        orderItem.setProductId(dto.getProductId());
        orderItem.setProductDescription(dto.getProductDescription());
        return orderItem;
    }

    private static OrderItemDTO getOrderItemDTOFromOrderItem(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setProductName(orderItem.getProductName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setProductId(orderItem.getProductId());
        dto.setProductDescription(orderItem.getProductDescription());
        return dto;
    }

    public static OrderPaymentResponseDTO getOrderPaymentResponseFromOrderResponseDTO(OrderResponseDTO orderResponseDTO){
        System.out.println("Order ID: " + orderResponseDTO.getId());
        System.out.println("User ID: " + orderResponseDTO.getUserId());
        System.out.println("Order Status: " + orderResponseDTO.getStatus());
        System.out.println("Order Total Amount: " + orderResponseDTO.getTotalAmount());

        OrderPaymentResponseDTO orderPaymentResponseDTO = new OrderPaymentResponseDTO();
        orderPaymentResponseDTO.setOrderId(orderResponseDTO.getId());
        orderPaymentResponseDTO.setUserId(orderResponseDTO.getUserId());
        orderPaymentResponseDTO.setPrice(orderResponseDTO.getTotalAmount());
        orderPaymentResponseDTO.setAddress(orderResponseDTO.getAddress());
        orderPaymentResponseDTO.setStatus(orderResponseDTO.getStatus());
        return orderPaymentResponseDTO;
    }

}