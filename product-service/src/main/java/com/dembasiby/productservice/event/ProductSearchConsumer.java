package com.dembasiby.productservice.event;

import com.dembasiby.productservice.document.ProductDocument;
import com.dembasiby.productservice.service.search.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ProductSearchConsumer {

    private static final Logger log = Logger.getLogger(ProductSearchConsumer.class.getName());
    private final ProductSearchService productSearchService;

    @KafkaListener(
            topics = "product-events",
            groupId = "product-search-group"
    )
    public void consume(ProductDocument document) {
        log.info("🔥 Received product from Kafka: " + document.getTitle());

        productSearchService.save(document);
    }
}
