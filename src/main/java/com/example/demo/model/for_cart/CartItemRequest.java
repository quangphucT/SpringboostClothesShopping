package com.example.demo.model.for_cart;

import lombok.Data;

@Data
public class CartItemRequest {
    Long productVariantId;
    Integer quantity;
}
