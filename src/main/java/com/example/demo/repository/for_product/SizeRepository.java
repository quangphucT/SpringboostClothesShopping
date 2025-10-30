package com.example.demo.repository.for_product;

import com.example.demo.entity.for_product.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Size findSizeByName(String name);
    List<Size> findAllByIsDeletedFalse();
    Size findSizeById(Long id);
}
