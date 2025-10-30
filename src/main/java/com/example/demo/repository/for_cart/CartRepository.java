package com.example.demo.repository.for_cart;

import com.example.demo.entity.for_order.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
