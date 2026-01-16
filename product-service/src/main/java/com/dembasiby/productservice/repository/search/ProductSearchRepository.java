package com.dembasiby.productservice.repository.search;

import com.dembasiby.productservice.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository
        extends ElasticsearchRepository<ProductDocument, Long> {

    Page<ProductDocument> findAllByIsDeletedFalse(Pageable pageable);

    Page<ProductDocument> findByCategoryNameAndIsDeletedFalse(
            String categoryName, Pageable pageable);
}
