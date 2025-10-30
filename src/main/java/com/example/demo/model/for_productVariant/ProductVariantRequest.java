package com.example.demo.model.for_productVariant;

import lombok.Data;

@Data
public class ProductVariantRequest {
    long colorId;
    long sizeId;
    Integer stockQuantity;
    Double price;
}
