package com.dembasiby.productservice.mapper;

import com.dembasiby.productservice.dto.product.ProductCategoryDto;
import com.dembasiby.productservice.dto.product.ProductDetailsDto;
import com.dembasiby.productservice.dto.product.ProductDto;
import com.dembasiby.productservice.dto.product.ProductSpecificationDto;
import com.dembasiby.productservice.model.Product;
import com.dembasiby.productservice.model.ProductSpecification;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

   public static Product toEntity(ProductDto productDto) {
       return null;
   }

   public static ProductDto toProductDto(Product product) {
       return new ProductDto(
               String.valueOf(product.getId()),
               product.getTitle(),
               product.getDescription(),
               String.valueOf(product.getPrice()),
               product.getImageUrl(),
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
       productDetailsDto.setPrice(String.valueOf(product.getPrice()));
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
               String.valueOf(product.getPrice()),
               product.getImageUrl()
       );
    }
}
