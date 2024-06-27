package com.example.userservice.producers;

import com.example.userservice.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEmailProducer {

    private static final String TOPIC = "email-topic";

    @Autowired
    private KafkaTemplate<String, EmailDTO> kafkaTemplate;

    public void sendEmail(EmailDTO emailDTO) {
        kafkaTemplate.send(TOPIC, emailDTO);
    }
}