package com.dembasiby.productservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_specifications")
public class ProductSpecification extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String specificationKey;
    private String specificationValue;

}