package com.dembasiby.productservice.controller;

import com.dembasiby.productservice.config.JwtAuthenticationFilter;
import com.dembasiby.productservice.config.JwtService;
import com.dembasiby.productservice.config.SecurityConfig;
import com.dembasiby.productservice.dto.product.*;
import com.dembasiby.productservice.exception.NotFoundException;
import com.dembasiby.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    private UsernamePasswordAuthenticationToken authToken() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void createProduct_returns201() throws Exception {
        CreateProductDto request = new CreateProductDto();
        request.setTitle("New Product");
        request.setDescription("Description");
        request.setPrice(new BigDecimal("49.99"));
        request.setImageUrl("http://example.com/img.jpg");
        request.setCategoryId(1L);

        ProductDto response = new ProductDto("1", "New Product", "Description",
                "http://example.com/img.jpg", new BigDecimal("49.99"), 1L, "Electronics");

        when(productService.createProduct(any(CreateProductDto.class))).thenReturn(response);

        mockMvc.perform(post("/")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Product"))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));
    }

    @Test
    void createProduct_returns403WhenUnauthenticated() throws Exception {
        CreateProductDto request = new CreateProductDto();
        request.setTitle("New Product");
        request.setDescription("Description");
        request.setPrice(new BigDecimal("49.99"));
        request.setImageUrl("http://example.com/img.jpg");
        request.setCategoryId(1L);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createProduct_returns400ForInvalidInput() throws Exception {
        CreateProductDto request = new CreateProductDto();

        mockMvc.perform(post("/")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductById_returns200() throws Exception {
        ProductDto response = new ProductDto("1", "Test Product", "Description",
                "http://example.com/img.jpg", new BigDecimal("99.99"), 1L, "Electronics");

        when(productService.getProduct(1L)).thenReturn(response);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void getProductById_returns404() throws Exception {
        when(productService.getProduct(99L)).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(get("/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllProducts_returns200() throws Exception {
        ProductDto dto = new ProductDto("1", "Test Product", "Description",
                "http://example.com/img.jpg", new BigDecimal("99.99"), 1L, "Electronics");
        Page<ProductDto> page = new PageImpl<>(List.of(dto));

        when(productService.getProducts(0, 10, "id", "ASC")).thenReturn(page);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Product"));
    }

    @Test
    void updateProduct_returns200() throws Exception {
        UpdateProductDto request = new UpdateProductDto("Updated", "Desc", new BigDecimal("149.99"), "url");
        ProductDto response = new ProductDto("1", "Updated", "Desc",
                "url", new BigDecimal("149.99"), 1L, "Electronics");

        when(productService.updateProduct(eq(1L), any(UpdateProductDto.class))).thenReturn(response);

        mockMvc.perform(put("/1")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void deleteProduct_returns200() throws Exception {
        mockMvc.perform(delete("/1")
                        .with(authentication(authToken())))
                .andExpect(status().isOk());
    }
}
