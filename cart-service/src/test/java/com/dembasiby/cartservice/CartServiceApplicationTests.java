package com.dembasiby.cartservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires MongoDB, Redis, and Kafka infrastructure")
class CartServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
