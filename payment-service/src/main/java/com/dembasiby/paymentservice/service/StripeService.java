package com.dembasiby.paymentservice.service;

import com.dembasiby.paymentservice.exception.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class StripeService {

    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                    .setCurrency(currency.toLowerCase())
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            log.info("Created Stripe PaymentIntent: {}", intent.getId());
            return intent;
        } catch (StripeException e) {
            log.error("Stripe payment failed: {}", e.getMessage());
            throw new PaymentException("Payment processing failed: " + e.getMessage());
        }
    }
}
