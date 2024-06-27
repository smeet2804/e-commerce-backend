package com.example.emailservice.consumer;

import com.example.emailservice.dto.EmailDTO;
import com.example.emailservice.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEmailConsumer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void listen(String message) {
        try {
            EmailDTO emailDTO = objectMapper.readValue(message, EmailDTO.class);
            System.out.println(emailDTO);
            emailService.sendSimpleMessage(emailDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
