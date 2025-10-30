package com.example.demo.model.for_cart;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CartResponse {
    long cartId;
    List<CartItemResponse> cartItemResponses;
}
