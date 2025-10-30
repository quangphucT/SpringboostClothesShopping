package com.example.demo.service.for_productVariant;

import com.example.demo.entity.for_product.Color;
import com.example.demo.entity.for_product.Size;
import com.example.demo.entity.for_productVariant.ProductVariant;
import com.example.demo.exception.NotEnoughException;
import com.example.demo.model.for_productVariant.UpdateProductVariantRequest;
import com.example.demo.repository.for_product.ColorRepository;
import com.example.demo.repository.for_product.SizeRepository;
import com.example.demo.repository.for_productVariant.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductVariantService {
    @Autowired
    ProductVariantRepository productVariantRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ColorRepository colorRepository;
    public void updateProductVariant (Long id, Long productId, UpdateProductVariantRequest updateProductVariantRequest){
        ProductVariant productVariant = productVariantRepository.findProductVariantByIdAndProductId(id, productId);
        if(productVariant == null){
            throw new NotEnoughException("Product variant not found");
        }else{
            productVariant.setStockQuantity(updateProductVariantRequest.getStockQuantity());
            productVariant.setPrice(updateProductVariantRequest.getPrice());
            Size size = sizeRepository.findSizeById(updateProductVariantRequest.getSizeId());
            productVariant.setSize(size);
            Color color = colorRepository.findColorById(updateProductVariantRequest.getColorId());
            productVariant.setColor(color);
            productVariantRepository.save(productVariant);
        }
    }
}
