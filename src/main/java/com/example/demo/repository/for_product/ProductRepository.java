package com.example.demo.repository.for_product;

import com.example.demo.entity.for_product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByIsDeletedIsFalse();
    Product findProductById(Long id);
}
