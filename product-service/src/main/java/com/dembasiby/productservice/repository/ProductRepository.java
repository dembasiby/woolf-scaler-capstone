package com.dembasiby.productservice.repository;

import com.dembasiby.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByIsDeletedFalse();
    List<Product> findAllByCategory_IdAndIsDeletedFalse(Long id);
    Optional<Product> findByIdAndIsDeletedFalse(Long id);
    boolean existsByCategoryIdAndIsDeletedFalse(Long categoryId);
}
