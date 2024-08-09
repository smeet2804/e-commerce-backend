package com.example.paymentservice.controllers;

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
    @Value("${stripe.apiKey}")
    private String apikey;
    public WebhookController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
            System.out.println(event.toString());
            System.out.println(stripeObject.toString());
            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    assert paymentIntent != null;
                    System.out.println(paymentIntent.toString());
                    assert paymentIntent != null;
                    System.out.println(paymentIntent.getAmount());
                    System.out.println(paymentIntent.getId());
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
