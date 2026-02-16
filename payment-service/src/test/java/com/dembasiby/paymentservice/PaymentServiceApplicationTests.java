package com.dembasiby.paymentservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires MySQL, Kafka, and Stripe infrastructure")
class PaymentServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
