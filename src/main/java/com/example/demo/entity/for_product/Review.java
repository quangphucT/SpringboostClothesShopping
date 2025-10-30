package com.example.demo.entity.for_product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Integer rating;
    String comment;
    Date createdAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
