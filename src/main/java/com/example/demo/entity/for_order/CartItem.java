package com.example.demo.entity.for_order;

import com.example.demo.entity.for_product.Product;
import com.example.demo.entity.for_productVariant.ProductVariant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;


    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    ProductVariant productVariant;

}
