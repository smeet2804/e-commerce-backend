package com.example.paymentservice.controllers;

import com.example.paymentservice.clients.OrderClient;
import com.example.paymentservice.models.OrderStatusDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.stripe.Stripe;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripeWebhook")
public class WebhookController {

    private final ObjectMapper objectMapper;
    private final OrderClient orderClient;
    @Value("${stripe.apiKey}")
    private String apikey;
    public WebhookController(ObjectMapper objectMapper, OrderClient orderClient) {
        this.objectMapper = objectMapper;
        this.orderClient = orderClient;
    }

    @PostMapping
    public void receiveWebHookEvents(@RequestBody String payload) {
        try {
            Stripe.apiKey = apikey;
            Event event = ApiResource.GSON.fromJson(payload, Event.class);
            System.out.println(event.getApiVersion()+" "+Stripe.API_VERSION);
            // Handle the event
            // Deserialize the nested object inside the event
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                // Deserialization failed, probably due to an API version mismatch.
                // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                // instructions on how to handle this case, or return an error here.
                System.out.println("No data object found");
            }
            System.out.println(event.getType());
//            System.out.println(stripeObject.toString());
            // Handle the event
            switch (event.getType().trim()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    assert paymentIntent != null;
                    String orderId = paymentIntent.getMetadata().get("orderId");
                    System.out.println("Order ID: "+orderId);
                    OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
                    orderStatusDTO.setStatus("PAID");
                    orderClient.updateOrderStatus(Long.parseLong(orderId), orderStatusDTO);
                    break;
                case "payment_method.attached":
                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                    // Then define and call a method to handle the successful attachment of a PaymentMethod.
                    // handlePaymentMethodAttached(paymentMethod);
                    break;
                // ... handle other event types
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }
        }
        catch (Exception e) {

        }
    }
}
