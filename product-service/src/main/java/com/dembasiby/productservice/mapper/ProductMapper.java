package com.dembasiby.productservice.mapper;

import com.dembasiby.productservice.document.ProductDocument;
import com.dembasiby.productservice.dto.product.*;
import com.dembasiby.productservice.model.Product;
import com.dembasiby.productservice.model.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

   public static Product toEntity(ProductDto productDto) {
       return null;
   }

   public static ProductDto toProductDto(Product product) {
       return new ProductDto(
               String.valueOf(product.getId()),
               product.getTitle(),
               product.getDescription(),
               product.getImageUrl(),
               product.getPrice(),
               product.getCategory().getId(),
               product.getCategory().getName()
       );
   }

   public static ProductDetailsDto toProductDetailsDto(Product product) {
       ProductDetailsDto productDetailsDto = new ProductDetailsDto();
       productDetailsDto.setId(String.valueOf(product.getId()));
       productDetailsDto.setTitle(product.getTitle());
       productDetailsDto.setDescription(product.getDescription());
       productDetailsDto.setImageUrl(product.getImageUrl());
       productDetailsDto.setCategoryId(product.getCategory().getId());
       productDetailsDto.setCategoryName(product.getCategory().getName());
       productDetailsDto.setPrice(product.getPrice());
       List<ProductSpecificationDto>  productSpecificationDtos = new ArrayList<>();

       for  (ProductSpecification productSpecification : product.getProductSpecifications()) {
           productSpecificationDtos.add(toProductSpecificationDto(productSpecification));
       }
       productDetailsDto.setProductSpecifications(productSpecificationDtos);

       return productDetailsDto;
   }

   public static ProductSpecificationDto toProductSpecificationDto(ProductSpecification productSpec) {
       return new ProductSpecificationDto(
               productSpec.getSpecificationKey(),
               productSpec.getSpecificationValue()
       );
   }

    public static ProductCategoryDto toProductCategoryDto(Product product) {
       return new ProductCategoryDto(
               String.valueOf(product.getId()),
               product.getTitle(),
               product.getDescription(),
               product.getPrice(),
               product.getImageUrl()
       );
    }

    public static ProductSearchDto toProductSearchDto(ProductDocument productDocument) {
        ProductSearchDto pSDto = new ProductSearchDto();
        pSDto.setId(String.valueOf(productDocument.getId()));
        pSDto.setTitle(productDocument.getTitle());
        pSDto.setDescription(productDocument.getDescription());
        pSDto.setImageUrl(productDocument.getImageUrl());
        pSDto.setPrice(BigDecimal.valueOf(productDocument.getPrice()));
        pSDto.setCategoryName(productDocument.getCategoryName());

        return pSDto;
    }

    public static Page<ProductSearchDto> toPage(SearchHits<ProductDocument> hits, Pageable pageable) {
       List<ProductSearchDto> dtos = hits.stream()
               .map(SearchHit::getContent)
               .map(ProductMapper::toProductSearchDto)
               .collect(Collectors.toList());

       return new PageImpl(dtos, pageable, hits.getTotalHits());
    }

    public static ProductDocument toProductDocument(Product product) {
        ProductDocument productDocument = new ProductDocument();
        productDocument.setId(product.getId());
        productDocument.setTitle(product.getTitle());
        productDocument.setDescription(product.getDescription());
        productDocument.setPrice(Double.valueOf(product.getPrice().toString()));
        productDocument.setImageUrl(product.getImageUrl());
        productDocument.setCategoryName(product.getCategory().getName());
        productDocument.setDeleted(product.isDeleted());

        return productDocument;
    }
}
