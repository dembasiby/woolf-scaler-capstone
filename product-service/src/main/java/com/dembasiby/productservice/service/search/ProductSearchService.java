package com.dembasiby.productservice.service.search;

import com.dembasiby.productservice.document.ProductDocument;
import org.springframework.data.domain.Page;

public interface ProductSearchService {

    void save(ProductDocument document);

    void delete(Long productId);

    Page<ProductDocument> search(String keyword, int page, int size);

    Page<ProductDocument> browse(int page, int size);

    Page<ProductDocument> filterByCategory(String category, int page, int size);
}
