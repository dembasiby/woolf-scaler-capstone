package com.dembasiby.productservice.controller.search;

import com.dembasiby.productservice.document.ProductDocument;
import com.dembasiby.productservice.service.search.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search/products")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping
    public Page<ProductDocument> browse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productSearchService.browse(page, size);
    }

    @GetMapping("/query")
    public Page<ProductDocument> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productSearchService.search(keyword, page, size);
    }

    @GetMapping("/category/{category}")
    public Page<ProductDocument> byCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productSearchService.filterByCategory(category, page, size);
    }
}
