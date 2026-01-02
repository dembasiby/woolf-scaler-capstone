package com.dembasiby.productservice.repository;

import com.dembasiby.productservice.model.Category;
import com.dembasiby.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByIsDeletedFalse(Pageable pageable);
    Page<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);
    Page<Product> findAllByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);
    //Page<Product> searchByKeywordAndIsDeletedFalse(String keyword, Pageable pageable);
    Optional<Product> findByIdAndIsDeletedFalse(Long id);
}
