package com.example.demo.repository.for_productVariant;

import com.example.demo.entity.for_productVariant.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {
    ProductVariant findProductVariantByIdAndProductId(Long id, Long productId);
}
