package com.dembasiby.productservice.repository;

import com.dembasiby.productservice.model.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    List<ProductSpecification> findAllByProductId(Long productId);
    void deleteByIdAndProductId(Long id, Long productId);
}
