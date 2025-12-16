package com.dembasiby.productservice.controller;

import com.dembasiby.productservice.dto.category.CategoryDto;
import com.dembasiby.productservice.dto.category.CreateCategoryDto;
import com.dembasiby.productservice.dto.category.UpdateCategoryDto;
import com.dembasiby.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        CategoryDto category = categoryService.createCategory(createCategoryDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(category.getId())
                .toUri();

        return ResponseEntity.created(location).body(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAllCategories() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }
}
