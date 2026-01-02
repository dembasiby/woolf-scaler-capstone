package com.dembasiby.productservice.service;

import com.dembasiby.productservice.dto.category.CategoryDto;
import com.dembasiby.productservice.dto.category.CreateCategoryDto;
import com.dembasiby.productservice.dto.category.UpdateCategoryDto;
import com.dembasiby.productservice.exception.ConflictException;
import com.dembasiby.productservice.exception.NotFoundException;
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
        Category category = categoryRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if  (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }

        Category updatedCategory = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toCategoryDto)
                .orElseThrow();
    }

    @Override
    public List<CategoryDto> getCategoryList() {
        return categoryRepository.findAllByIsDeletedFalse().stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (!category.getFeaturedProducts().isEmpty()) {
           throw new ConflictException("Category has active products");
        }

        category.setDeleted(true);
    }
}
