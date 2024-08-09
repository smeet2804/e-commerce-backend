package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.PaymentRequestDTO;
import com.example.paymentservice.services.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/generate")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequestDTO paymentRequestDTO){

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: "+authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            requestAttributes.setAttribute("Authorization", token, RequestAttributes.SCOPE_REQUEST);

            try {
                String paymentUrl = paymentService.generatePayment(paymentRequestDTO);
                return ResponseEntity.ok(paymentUrl);

            }
            catch (StripeException se) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Stripe error occurred: " + se.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }

}