package com.dembasiby.productservice.service;

import com.dembasiby.productservice.document.ProductDocument;
import com.dembasiby.productservice.dto.product.*;
import com.dembasiby.productservice.exception.NotFoundException;
import com.dembasiby.productservice.model.Category;
import com.dembasiby.productservice.model.Product;
import com.dembasiby.productservice.model.ProductSpecification;
import com.dembasiby.productservice.repository.CategoryRepository;
import com.dembasiby.productservice.repository.ProductRepository;
import com.dembasiby.productservice.repository.ProductSpecificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductSpecificationRepository productSpecificationRepository;
    @Mock
    private KafkaTemplate<String, ProductDocument> kafkaTemplate;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setTitle("Test Product");
        testProduct.setDescription("A test product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setImageUrl("http://example.com/image.jpg");
        testProduct.setCategory(testCategory);
        testProduct.setProductSpecifications(new ArrayList<>());
        testProduct.setDeleted(false);
    }

    @Test
    void createProduct_success() {
        CreateProductDto dto = new CreateProductDto();
        dto.setTitle("New Product");
        dto.setDescription("Description");
        dto.setPrice(new BigDecimal("49.99"));
        dto.setImageUrl("http://example.com/new.jpg");
        dto.setCategoryId(1L);

        when(categoryRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        ProductDto result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("New Product", result.getTitle());
        assertEquals("Electronics", result.getCategoryName());
        verify(kafkaTemplate).send(eq("product-events"), any(ProductDocument.class));
    }

    @Test
    void createProduct_categoryNotFound() {
        CreateProductDto dto = new CreateProductDto();
        dto.setCategoryId(99L);

        when(categoryRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.createProduct(dto));
    }

    @Test
    void getProduct_success() {
        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testProduct));

        ProductDto result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        assertEquals(1L, result.getCategoryId());
    }

    @Test
    void getProduct_notFound() {
        when(productRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProduct(99L));
    }

    @Test
    void getProductDetails_success() {
        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testProduct));

        ProductDetailsDto result = productService.getProductDetails(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        assertNotNull(result.getProductSpecifications());
    }

    @Test
    void getProducts_returnsPaginatedResults() {
        Page<Product> page = new PageImpl<>(List.of(testProduct), PageRequest.of(0, 10), 1);
        when(productRepository.findAllByIsDeletedFalse(any(PageRequest.class))).thenReturn(page);

        Page<ProductDto> result = productService.getProducts(0, 10, "id", "asc");

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getTitle());
    }

    @Test
    void updateProduct_success() {
        UpdateProductDto dto = new UpdateProductDto("Updated Title", "Updated Desc", new BigDecimal("149.99"), "http://example.com/updated.jpg");

        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDto result = productService.updateProduct(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Title", testProduct.getTitle());
        assertEquals(new BigDecimal("149.99"), testProduct.getPrice());
        verify(kafkaTemplate).send(eq("product-events"), any(ProductDocument.class));
    }

    @Test
    void updateProduct_notFound() {
        UpdateProductDto dto = new UpdateProductDto("Title", "Desc", new BigDecimal("10.00"), "url");

        when(productRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(99L, dto));
    }

    @Test
    void deleteProduct_success() {
        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.deleteProduct(1L);

        assertTrue(testProduct.isDeleted());
        verify(kafkaTemplate).send(eq("product-events"), any(ProductDocument.class));
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(99L));
    }

    @Test
    void addSpecificationToProduct_success() {
        CreateProductSpecificationDto specDto = new CreateProductSpecificationDto("Color", "Red");

        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testProduct));
        when(productSpecificationRepository.save(any(ProductSpecification.class))).thenAnswer(invocation -> {
            ProductSpecification spec = invocation.getArgument(0);
            spec.setId(1L);
            return spec;
        });
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDetailsDto result = productService.addSpecificationToProduct(1L, specDto);

        assertNotNull(result);
        verify(productSpecificationRepository).save(any(ProductSpecification.class));
        verify(kafkaTemplate).send(eq("product-events"), any(ProductDocument.class));
    }

    @Test
    void removeSpecificationFromProduct_success() {
        when(productRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(testProduct));

        productService.removeSpecificationFromProduct(10L, 1L);

        verify(productSpecificationRepository).deleteByIdAndProductId(10L, 1L);
        verify(kafkaTemplate).send(eq("product-events"), any(ProductDocument.class));
    }

    @Test
    void listSpecifications_success() {
        ProductSpecification spec = new ProductSpecification();
        spec.setSpecificationKey("Weight");
        spec.setSpecificationValue("1kg");
        spec.setProduct(testProduct);

        when(productSpecificationRepository.findAllByProductId(1L)).thenReturn(List.of(spec));

        List<ProductSpecificationDto> result = productService.listSpecifications(1L);

        assertEquals(1, result.size());
        assertEquals("Weight", result.get(0).getSpecKey());
        assertEquals("1kg", result.get(0).getSpecValue());
    }
}
