package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.category.CategoryDto;
import com.dembasiby.productservice.dto.category.CreateCategoryDto;
import com.dembasiby.productservice.dto.category.UpdateCategoryDto;
import com.dembasiby.productservice.mapper.CategoryMapper;
import com.dembasiby.productservice.model.Category;
import com.dembasiby.productservice.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        Category category = new Category();
        category.setName(createCategoryDto.getName());
        categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, UpdateCategoryDto categoryDto) {
        return null;
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return null;
    }

    @Override
    public List<CategoryDto> getCategoryList() {
        return categoryRepository.findAllByIsDeletedFalse().stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public void deleteCategory(Long id) {

    }
}
