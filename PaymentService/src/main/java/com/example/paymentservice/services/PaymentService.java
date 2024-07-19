package com.example.paymentservice.services;
import com.example.paymentservice.clients.OrderClient;
import com.example.paymentservice.dtos.OrderDTO;
import com.example.paymentservice.dtos.PaymentRequestDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    @Value("${stripe.apiKey}")
    private String apikey;

    @Autowired
    private OrderClient orderClient;

    public String generatePayment(PaymentRequestDTO paymentRequestDTO) throws StripeException {
        String orderId = paymentRequestDTO.getOrderId();
        Stripe.apiKey = apikey;

        // Fetch order details from OrderService
        OrderDTO order = orderClient.getOrderById(orderId);
        Long amount = order.getPrice();

        Price price = getPrice(amount);

        PaymentLinkCreateParams params =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .build();

        PaymentLink paymentLink = PaymentLink.create(params);

        // Update order status to PROCESSING
        orderClient.updateOrderStatus(orderId, "COMPLETED");

        return paymentLink.getUrl();
    }

    private Price getPrice(Long amount) throws StripeException {
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