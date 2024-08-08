package com.example.orderservice.producers;

import com.example.orderservice.dtos.OrderStatusEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderStatusEmailProducer {

    private static final String TOPIC = "email-topic";

    @Autowired
    private KafkaTemplate<String, OrderStatusEmailDTO> kafkaTemplate;

    public void sendEmail(OrderStatusEmailDTO orderStatusEmailDTO) {
        kafkaTemplate.send(TOPIC, orderStatusEmailDTO);
    }

}
