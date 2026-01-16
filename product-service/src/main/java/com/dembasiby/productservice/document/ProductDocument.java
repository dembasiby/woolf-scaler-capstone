package com.dembasiby.productservice.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter @Setter
@Document(indexName = "products")
@Setting(settingPath = "elasticsearch/product-settings.json")
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "english")
    private String description;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String imageUrl;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Boolean)
    private boolean isDeleted;
}
