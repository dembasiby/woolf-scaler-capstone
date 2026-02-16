package com.dembasiby.productservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires Elasticsearch and Kafka infrastructure")
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
