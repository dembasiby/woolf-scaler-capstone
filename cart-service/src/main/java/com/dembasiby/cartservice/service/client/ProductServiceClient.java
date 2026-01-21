package com.dembasiby.cartservice.service.client;

import com.dembasiby.cartservice.dto.response.product.ProductSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductServiceClient {
    private final RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceBaseUrl;

    @Autowired
    public ProductServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductSummaryDto getProduct(String productId) {
        try {
            String url = productServiceBaseUrl + "/" + productId;
            return restTemplate.getForObject(url, ProductSummaryDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch product: " + productId, e);
        }
    }
}
