package com.example.demo.repository.for_product;

import com.example.demo.entity.for_product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
       List<Category> findAllByIsDeletedFalse();
}
