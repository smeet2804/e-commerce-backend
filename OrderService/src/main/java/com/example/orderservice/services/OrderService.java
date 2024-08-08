package com.example.orderservice.services;

import com.example.orderservice.clients.UserClient;
import com.example.orderservice.dtos.CartDTO;
import com.example.orderservice.dtos.OrderRequestDTO;
import com.example.orderservice.dtos.OrderResponseDTO;
import com.example.orderservice.dtos.OrderStatusEmailDTO;
import com.example.orderservice.mappers.OrderMapper;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderItem;
import com.example.orderservice.models.OrderStatus;
import com.example.orderservice.producers.KafkaOrderStatusEmailProducer;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceI {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaOrderStatusEmailProducer kafkaOrderStatusEmailProducer;

    @Autowired
    private UserClient userClient;

    @Override
    public OrderResponseDTO createOrder(CartDTO cartDTO) {
        Order order = new Order();
        List<OrderItem> orderItems = cartDTO.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setProductName(cartItem.getProductName());
                    orderItem.setProductDescription(cartItem.getProductDescription());
                    return orderItem;
                })
                .collect(Collectors.toList());
        order.setUserId(cartDTO.getUserId());
        order.setItems(orderItems);
        order.setAddress(cartDTO.getAddress());
        order.setPaymentMethod(cartDTO.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);
        System.out.println("Total Price cart: "+cartDTO.getTotalPrice());
        order.setTotalAmount(cartDTO.getTotalPrice());
        orderRepository.save(order);
        sendEmailUpdateOrderStatus(cartDTO.getUserId(), order.getId(), order.getStatus().toString());
        return OrderMapper.getOrderResponseDTOFromOrder(order);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return order != null ? OrderMapper.getOrderResponseDTOFromOrder(order) : null;
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderMapper::getOrderResponseDTOFromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.valueOf(status));
            orderRepository.save(order);

            // Fetch the userId from the order object
            Long userId = order.getUserId();

            // Use UserClient to fetch user details

            sendEmailUpdateOrderStatus(userId, orderId, status);

        }
        return order != null ? OrderMapper.getOrderResponseDTOFromOrder(order) : null;
    }

    public void sendEmailUpdateOrderStatus(Long userId, Long orderId, String orderStatus) {
        String userDetails = userClient.getUserDetails(userId);

        try{

            JSONObject userJson = new JSONObject(userDetails);
            String email = userJson.getString("email");
            String firstName = userJson.getString("firstName");
            String lastName = userJson.getString("lastName");
            OrderStatusEmailDTO orderStatusEmailDTO = new OrderStatusEmailDTO();
            orderStatusEmailDTO.setTo(email);
            orderStatusEmailDTO.setSubject("Order Status Update for " + orderId);
            orderStatusEmailDTO.setText("Order number: "+orderId+"\nYour order status has been updated to: " + orderStatus);

            kafkaOrderStatusEmailProducer.sendEmail(orderStatusEmailDTO);
        }
        catch (JSONException e){

        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
