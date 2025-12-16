package com.dembasiby.productservice.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CreateCategoryDto {
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;
}
