package com.dembasiby.productservice.service.search;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.dembasiby.productservice.document.ProductDocument;
import com.dembasiby.productservice.repository.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductSearchRepository repository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void save(ProductDocument document) {
        elasticsearchTemplate.save(document);
    }

    @Override
    public void delete(Long productId) {
        elasticsearchTemplate.delete(String.valueOf(productId), ProductDocument.class);
    }

    @Override
    public Page<ProductDocument> search(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Query esQuery;
        if (keyword == null || keyword.trim().isEmpty()) {
            // Match all non-deleted products
            esQuery = BoolQuery.of(b -> b
                    .mustNot(TermQuery.of(t -> t.field("isDeleted").value(true)))
            )._toQuery();
        } else {
            // Full-text search on title and description
            MultiMatchQuery multiMatch = MultiMatchQuery.of(m -> m
                    .query(keyword)
                    .fields("title^2", "description")
                    .type(TextQueryType.BestFields)
                    .fuzziness("AUTO")
            );

            esQuery = BoolQuery.of(b -> b
                    .must(multiMatch._toQuery())
                    .mustNot(TermQuery.of(t -> t.field("isDeleted").value(true)))
            )._toQuery();
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(esQuery)
                .withPageable(pageable)
                .build();

        var hits = elasticsearchTemplate.search(nativeQuery, ProductDocument.class);

        List<ProductDocument> content = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }
    
    @Override
    public Page<ProductDocument> browse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByIsDeletedFalse(pageable);
    }

    @Override
    public Page<ProductDocument> filterByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCategoryNameAndIsDeletedFalse(category, pageable);
    }
}
