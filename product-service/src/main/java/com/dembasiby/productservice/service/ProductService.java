package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.product.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(CreateProductDto createProductDto);
    ProductDto getProduct(Long id);
    ProductDetailsDto getProductDetails(Long id);
    Page<ProductDto> getProducts(int pageNumber, int pageSize,
                                 String sortBy, String sortDirection);
    Page<ProductCategoryDto> getProductsByCategory(
            Long categoryId, int pageNumber, int pageSize,
            String sortBy,String sortDirection);
    ProductDto updateProduct(Long id, UpdateProductDto updateProductDto);
    void deleteProduct(Long id);
    ProductDetailsDto addSpecificationToProduct(Long productId, CreateProductSpecificationDto productSpecDto);
    void removeSpecificationFromProduct(Long productId, Long specId);
    List<ProductSpecificationDto> listSpecifications(Long productId);
}
