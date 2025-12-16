package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.CategoryDto;
import com.dembasiby.productservice.dto.CreateCategoryDto;
import com.dembasiby.productservice.dto.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CreateCategoryDto createCategoryDto);
    CategoryDto updateCategory(Long id, UpdateCategoryDto categoryDto);
    CategoryDto getCategory(Long id);
    List<CategoryDto> getCategoryList();
    void deleteCategory(Long id);
}
