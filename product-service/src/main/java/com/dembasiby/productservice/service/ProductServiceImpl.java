package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.product.CreateProductDto;
import com.dembasiby.productservice.dto.product.ProductDto;
import com.dembasiby.productservice.dto.product.UpdateProductDto;
import com.dembasiby.productservice.mapper.ProductMapper;
import com.dembasiby.productservice.model.Category;
import com.dembasiby.productservice.model.Product;
import com.dembasiby.productservice.repository.CategoryRepository;
import com.dembasiby.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductDto createProduct(CreateProductDto createProductDto) {
        Category category = categoryRepository.getReferenceById(
                (long) createProductDto.getCategoryId());

        Product product = new Product();
        product.setTitle(createProductDto.getTitle());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        product.setImageUrl(createProductDto.getImageUrl());
        product.setCategory(category);

        productRepository.save(product);

        return ProductMapper.toProductDto(product);
    }

    @Override
    public ProductDto getProduct(Long id) {
        return null;
    }

    @Override
    public List<ProductDto> getProducts() {
        return List.of();
    }

    @Override
    public ProductDto updateProduct(Long id, UpdateProductDto updateProductDto) {
        return null;
    }

    @Override
    public ProductDto updateProduct(UpdateProductDto updateProductDto) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
}
