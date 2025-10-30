package com.example.demo.repository.for_product;

import com.example.demo.entity.for_product.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Color findColorByName(String name);
    List<Color>findAllByIsDeletedFalse();
    Color findColorById(Long id);
}
