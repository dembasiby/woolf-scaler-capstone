package com.dembasiby.notificationservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires MySQL, Kafka, and SES infrastructure")
class NotificationServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
