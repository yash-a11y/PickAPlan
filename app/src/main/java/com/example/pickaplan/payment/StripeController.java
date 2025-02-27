package com.example.pickaplan.payment;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> paymentRequest) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // Create a customer
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description", "Customer for Pick A Plan");
            Customer customer = Customer.create(customerParams);

            // Create an ephemeral key
            Map<String, Object> ephemeralKeyParams = new HashMap<>();
            ephemeralKeyParams.put("customer", customer.getId());
            EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);

            // Create payment intent
            long amount = Long.parseLong(paymentRequest.get("amount").toString());
            String currency = paymentRequest.get("currency").toString();

            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setCustomer(customer.getId())
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(createParams);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("customer", customer.getId());
            response.put("ephemeralKey", ephemeralKey.getSecret());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
} 