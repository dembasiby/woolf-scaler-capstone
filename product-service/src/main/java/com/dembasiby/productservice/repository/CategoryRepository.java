package com.dembasiby.productservice.repository;

import com.dembasiby.productservice.dto.product.ProductCategoryDto;
import com.dembasiby.productservice.model.Category;
import com.dembasiby.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsDeletedFalse();
    Optional<Category> findByIdAndIsDeletedFalse(Long id);
    List<ProductCategoryDto> getFeaturedProductsByCategoryId(Long id);
}
