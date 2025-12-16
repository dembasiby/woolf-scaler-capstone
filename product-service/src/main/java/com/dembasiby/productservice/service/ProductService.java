package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.CreateProductDto;
import com.dembasiby.productservice.dto.ProductDto;
import com.dembasiby.productservice.dto.UpdateProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(CreateProductDto createProductDto);
    ProductDto getProduct(Long id);
    List<ProductDto> getProducts();
    ProductDto updateProduct(Long id, UpdateProductDto updateProductDto);
    ProductDto updateProduct(UpdateProductDto updateProductDto);
    void deleteProduct(Long id);
}
