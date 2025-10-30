package com.example.demo.repository.for_product;

import com.example.demo.entity.for_product.ProductionImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagesRepository extends JpaRepository<ProductionImage,Long> {
    ProductionImage findProductImageByIdAndProductId(Long id, Long productId);
}
