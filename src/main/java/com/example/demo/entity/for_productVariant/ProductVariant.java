package com.example.demo.entity.for_productVariant;

import com.example.demo.entity.for_order.CartItem;
import com.example.demo.entity.for_product.Color;
import com.example.demo.entity.for_product.Product;
import com.example.demo.entity.for_product.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    Product product;


    private Integer stockQuantity;  // ✅ tồn kho riêng từng biến thể

    private Double price;           // có thể giá khác nhau từng biến thể

    @ManyToOne
    @JoinColumn(name = "color_id")

    Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    Size size;

    @OneToMany(mappedBy = "productVariant")
            @JsonIgnore
    List<CartItem> cartItems;
}

