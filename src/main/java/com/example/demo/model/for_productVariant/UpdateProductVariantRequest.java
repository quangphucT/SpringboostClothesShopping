package com.example.demo.model.for_productVariant;

import lombok.Data;

@Data
public class UpdateProductVariantRequest {
    Double price;
    Integer stockQuantity;
    Long colorId;
    Long sizeId;
}
