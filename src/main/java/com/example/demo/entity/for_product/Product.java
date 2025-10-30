package com.example.demo.entity.for_product;

import com.example.demo.entity.for_order.CartItem;
import com.example.demo.entity.for_order.OrderItem;
import com.example.demo.entity.for_productVariant.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotBlank(message = "Name must not be blank!")
    String name;
    @NotBlank(message = "Description must not be blank!")
    String description;
    @NotBlank(message = "Thumbnail must not be blank!")
    String thumbnail;  // ảnh đại diện\
    Date created_at;
    Date updated_at;
    @Schema(defaultValue = "false")
    Boolean isDeleted = false;

   @ManyToOne
   @JoinColumn(name = "category_id")
    Category category;

   @OneToMany(mappedBy = "product")
           @JsonIgnore
    List<OrderItem> order_items;

   @OneToMany(mappedBy = "product")
           @JsonIgnore
    List<Review> reviews;

   @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
           @JsonIgnore
    List<ProductionImage>  production_images;




    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)

    List<ProductVariant> productVariants;
}
