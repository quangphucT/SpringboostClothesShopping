package com.example.demo.model.for_cart;

import lombok.Data;

@Data
public class CartItemResponse {
    long id;
    int quantity;
    // thông tin biến thể sản phẩm
    private long productVariantId;
    private String colorName;
    private String sizeName;
    private double price;

    // thumbnail và tên sản phẩm (lấy qua productVariant.product)
    private String productName;
    private String productThumbnail;

}
