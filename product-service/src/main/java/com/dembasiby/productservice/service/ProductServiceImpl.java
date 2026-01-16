package com.dembasiby.productservice.service;

import com.dembasiby.productservice.document.ProductDocument;
import com.dembasiby.productservice.dto.product.*;
import com.dembasiby.productservice.exception.NotFoundException;
import com.dembasiby.productservice.mapper.ProductMapper;
import com.dembasiby.productservice.model.Category;
import com.dembasiby.productservice.model.Product;
import com.dembasiby.productservice.model.ProductSpecification;
import com.dembasiby.productservice.repository.CategoryRepository;
import com.dembasiby.productservice.repository.ProductRepository;
import com.dembasiby.productservice.repository.ProductSpecificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductSpecificationRepository productSpecificationRepository;
    private final KafkaTemplate<String, ProductDocument> kafkaTemplate;

    private static final String TOPIC = "product-events";

    @Override
    public ProductDto createProduct(CreateProductDto createProductDto) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(
                createProductDto.getCategoryId()).orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = new Product();
        product.setTitle(createProductDto.getTitle());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        product.setImageUrl(createProductDto.getImageUrl());
        product.setCategory(category);

        productRepository.save(product);

        // Publish to ES via Kafka
        kafkaTemplate.send(TOPIC, ProductMapper.toProductDocument(product));

        return ProductMapper.toProductDto(product);
    }

    @Override
    public ProductDto getProduct(Long id) {
        return productRepository.findByIdAndIsDeletedFalse(id)
                .map(ProductMapper::toProductDto)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public ProductDetailsDto getProductDetails(Long id) {
        return productRepository.findByIdAndIsDeletedFalse(id)
                .map(ProductMapper::toProductDetailsDto)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public Page<ProductDto> getProducts(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findAllByIsDeletedFalse(pageRequest)
                .map(ProductMapper::toProductDto);
    }

    @Override
    public Page<ProductCategoryDto> getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findAllByCategoryIdAndIsDeletedFalse(categoryId, pageRequest)
                .map(ProductMapper::toProductCategoryDto);
    }

    @Override
    public ProductDto updateProduct(Long id, UpdateProductDto updateProductDto) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setTitle(updateProductDto.getTitle());
        product.setDescription(updateProductDto.getDescription());
        product.setPrice(updateProductDto.getPrice());
        product.setImageUrl(updateProductDto.getImageUrl());

        productRepository.save(product);

        // Publish to ES via Kafka
        kafkaTemplate.send(TOPIC, ProductMapper.toProductDocument(product));

        return ProductMapper.toProductDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setDeleted(true);
        productRepository.save(product);

        // Mark as deleted in ES
        ProductDocument doc = ProductMapper.toProductDocument(product);
        doc.setDeleted(true);
        kafkaTemplate.send(TOPIC, doc);
    }

    @Transactional
    @Override
    public ProductDetailsDto addSpecificationToProduct(Long productId, CreateProductSpecificationDto productSpecDto) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setProduct(product);
        productSpecification.setSpecificationKey(productSpecDto.getSpecKey());
        productSpecification.setSpecificationValue(productSpecDto.getSpecValue());
        productSpecificationRepository.save(productSpecification);

        product.getProductSpecifications().add(productSpecification);
        productRepository.save(product);

        // Update ES index
        kafkaTemplate.send(TOPIC, ProductMapper.toProductDocument(product));

        return ProductMapper.toProductDetailsDto(product);
    }

    @Transactional
    @Override
    public void removeSpecificationFromProduct(Long specId, Long productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        productSpecificationRepository.deleteByIdAndProductId(specId, product.getId());

        // Update ES index
        kafkaTemplate.send(TOPIC, ProductMapper.toProductDocument(product));
    }

    @Override
    public List<ProductSpecificationDto> listSpecifications(Long productId) {
        return productSpecificationRepository.findAllByProductId(productId)
                .stream().map(ProductMapper::toProductSpecificationDto)
                .toList();
    }
}
