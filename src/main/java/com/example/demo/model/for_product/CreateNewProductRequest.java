package com.example.demo.model.for_product;

import com.example.demo.model.for_productVariant.ProductVariantRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewProductRequest {
    @NotBlank(message = "Name must not be blank!")
    String name;

    @NotBlank(message = "Description must not be blank!")
    String description;


    @NotBlank(message = "Thumbnail must not be blank!")
    String thumbnail;

    Long categoryId;
    List<String> productionImages;


    List<ProductVariantRequest> productVariantRequests;


 }
