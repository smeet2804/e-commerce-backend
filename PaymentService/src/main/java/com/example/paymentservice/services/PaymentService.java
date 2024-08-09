package com.example.paymentservice.services;
import com.example.paymentservice.clients.OrderClient;
import com.example.paymentservice.dtos.OrderDTO;
import com.example.paymentservice.dtos.PaymentRequestDTO;
import com.example.paymentservice.models.OrderStatusDTO;
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
        Long orderId = paymentRequestDTO.getOrderId();
        Stripe.apiKey = apikey;

        // Fetch order details from OrderService
        OrderDTO order = orderClient.getOrderDetailsForPayment(orderId);
        System.out.println(order.getOrderId());
        System.out.println(order.getAddress());
        double amount = order.getPrice();
        System.out.println("Payment Amount is: "+amount);
        Price price = getPrice((long) amount);

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
//        OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
//        orderStatusDTO.setStatus("PROCESSING");
//        orderClient.updateOrderStatus(orderId, orderStatusDTO);

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