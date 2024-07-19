package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.PaymentRequestDTO;
import com.example.paymentservice.services.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/generate")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequestDTO paymentRequestDTO){

        try {
            String paymentUrl = paymentService.generatePayment(paymentRequestDTO);
            return ResponseEntity.ok(paymentUrl);

        }
        catch (StripeException se) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Stripe error occurred: " + se.getMessage());
        }
    }

}