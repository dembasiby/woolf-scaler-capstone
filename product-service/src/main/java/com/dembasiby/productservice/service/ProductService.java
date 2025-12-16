package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.product.CreateProductDto;
import com.dembasiby.productservice.dto.product.ProductDto;
import com.dembasiby.productservice.dto.product.UpdateProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(CreateProductDto createProductDto);
    ProductDto getProduct(Long id);
    List<ProductDto> getProducts();
    ProductDto updateProduct(Long id, UpdateProductDto updateProductDto);
    ProductDto updateProduct(UpdateProductDto updateProductDto);
    void deleteProduct(Long id);
}
