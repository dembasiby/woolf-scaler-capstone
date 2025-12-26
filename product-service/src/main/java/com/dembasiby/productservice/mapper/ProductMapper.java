package com.dembasiby.productservice.mapper;

import com.dembasiby.productservice.dto.product.ProductCategoryDto;
import com.dembasiby.productservice.dto.product.ProductDto;
import com.dembasiby.productservice.model.Product;

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
