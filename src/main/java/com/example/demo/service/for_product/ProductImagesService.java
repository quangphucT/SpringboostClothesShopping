package com.example.demo.service.for_product;

import com.example.demo.entity.for_product.ProductionImage;
import com.example.demo.exception.NotEnoughException;
import com.example.demo.model.for_product.UpdateProductImagesRequest;
import com.example.demo.repository.for_product.ProductImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImagesService {
    @Autowired
    ProductImagesRepository productImagesRepository;
    public void updateProductImage(Long id, Long productId, UpdateProductImagesRequest updateProductImagesRequest) {
        ProductionImage productionImage = productImagesRepository.findProductImageByIdAndProductId(id, productId);
        if (productionImage != null) {
            productionImage.setImageUrl(updateProductImagesRequest.getImageUrl());
            productImagesRepository.save(productionImage);
        }else{
            throw new NotEnoughException("Product image not found");
        }
    }
}
