package com.dembasiby.productservice.controller;

import com.dembasiby.productservice.dto.CreateProductDto;
import com.dembasiby.productservice.dto.ProductDto;
import com.dembasiby.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto product) {
        ProductDto productDto = productService.createProduct(product);
        return ResponseEntity
                .created(URI.create("/api/products" + productDto.getId()))
                .body(productDto);
    }
}
