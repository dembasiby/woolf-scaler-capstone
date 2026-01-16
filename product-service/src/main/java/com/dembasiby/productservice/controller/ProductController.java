package com.dembasiby.productservice.controller;

import com.dembasiby.productservice.dto.product.*;
import com.dembasiby.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto product) {
        ProductDto productDto = productService.createProduct(product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(productDto);
    }

    @PostMapping("/{id}/specifications")
    public ResponseEntity<ProductDetailsDto> addSpecificationToProduct(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductSpecificationDto productSpecificationDto) {
        ProductDetailsDto productDetailsDto = productService.addSpecificationToProduct(id, productSpecificationDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}/details")
                .buildAndExpand(productDetailsDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(productDetailsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ProductDetailsDto> getProductDetails(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetails(id));
    }

    @GetMapping("/{id}/specifications")
    public ResponseEntity<List<ProductSpecificationDto>> getProductSpecifications(@PathVariable Long id) {
        return ResponseEntity.ok(productService.listSpecifications(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(productService.getProducts(
                pageNumber, pageSize, sortBy, sortDirection));
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Page<ProductCategoryDto>> getAllProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {

        return ResponseEntity.ok(productService.getProductsByCategory(
                categoryId, pageNumber, pageSize,
                sortBy, sortDirection));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto>  updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }



    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @DeleteMapping("/{productId}/specifications/{specId}")
    public void deleteSpecification(@PathVariable Long productId, @PathVariable Long specId) {
        productService.removeSpecificationFromProduct(productId, specId);
    }
}
