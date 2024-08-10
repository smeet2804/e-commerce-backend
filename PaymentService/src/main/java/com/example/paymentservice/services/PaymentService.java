package com.example.paymentservice.services;

import com.example.paymentservice.clients.OrderClient;
import com.example.paymentservice.dtos.OrderDTO;
import com.example.paymentservice.dtos.PaymentRequestDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.apiKey}")
    private String apikey;

    @Autowired
    private OrderClient orderClient;

    public String generatePayment(PaymentRequestDTO paymentRequestDTO) throws StripeException {
        Long orderId = paymentRequestDTO.getOrderId();
        Stripe.apiKey = apikey;

        // Fetch order details from OrderService
        OrderDTO order = orderClient.getOrderDetailsForPayment(orderId);
        double amount = order.getPrice();
        System.out.println("Payment Amount is: " + amount);

        // Create Price object
        Price price = createPrice((long) (amount * 100));

        // Create metadata for the PaymentIntent

//        // Create PaymentIntent
//        PaymentIntentCreateParams intentParams =
//                PaymentIntentCreateParams.builder()
//                        .setAmount((long) (amount * 100)) // amount in cents
//                        .setCurrency("usd")
//                        .putAllMetadata(metadata)
//                        .setDescription("Payment for Order #" + orderId)
//                        .build();
//
//        PaymentIntent paymentIntent = PaymentIntent.create(intentParams);

        // Create PaymentLink
        PaymentLinkCreateParams linkParams =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setPaymentIntentData(
                                PaymentLinkCreateParams.PaymentIntentData.builder()
                                        .putMetadata("orderId", orderId.toString())
//                                        .putMetadata("userEmail", order.getUserEmail())
                                        .build()
                        )
                        .putMetadata("orderId", orderId.toString())
                        .build();

        PaymentLink paymentLink = PaymentLink.create(linkParams);

        // Optional: Update order status to PROCESSING
        // OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
        // orderStatusDTO.setStatus("PROCESSING");
        // orderClient.updateOrderStatus(orderId, orderStatusDTO);

        return paymentLink.getUrl();
    }

    private Price createPrice(Long amount) throws StripeException {
        PriceCreateParams params =
                PriceCreateParams.builder()
                        .setCurrency("usd")
                        .setUnitAmount(amount)
                        .setProductData(
                                PriceCreateParams.ProductData.builder().setName("Gold Plan").build()
                        )
                        .build();
        return Price.create(params);
    }
}
