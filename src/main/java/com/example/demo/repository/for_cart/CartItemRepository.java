package com.example.demo.repository.for_cart;

import com.example.demo.entity.for_order.Cart;
import com.example.demo.entity.for_order.CartItem;
import com.example.demo.entity.for_productVariant.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductVariant(Cart cart, ProductVariant productVariant);
}
