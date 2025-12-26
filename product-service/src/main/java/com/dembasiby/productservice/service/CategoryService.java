package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.category.CategoryDto;
import com.dembasiby.productservice.dto.category.CreateCategoryDto;
import com.dembasiby.productservice.dto.category.UpdateCategoryDto;
import com.dembasiby.productservice.dto.product.ProductCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto createCategoryDto);
    CategoryDto updateCategory(Long id, UpdateCategoryDto categoryDto);
    CategoryDto getCategory(Long id);
    List<CategoryDto> getCategoryList();
    List<ProductCategoryDto> getCategoryProducts(Long id);
    void deleteCategory(Long id);
}
