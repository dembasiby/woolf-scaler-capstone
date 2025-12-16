package com.dembasiby.productservice.controller;

import com.dembasiby.productservice.dto.category.CategoryDto;
import com.dembasiby.productservice.dto.category.CreateCategoryDto;
import com.dembasiby.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
