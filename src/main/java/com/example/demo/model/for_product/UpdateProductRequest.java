package com.example.demo.model.for_product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProductRequest {
    @NotBlank(message = "Name must not be blank!")
    String name;

    @NotBlank(message = "Description must not be blank!")
    String description;


    @NotBlank(message = "Thumbnail must not be blank!")
    String thumbnail;

    Long categoryId;
}
