package com.example.demo.api.for_product;


import com.example.demo.model.for_product.UpdateProductImagesRequest;
import com.example.demo.service.for_product.ProductImagesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ProductImagesAPI {
    @Autowired
    ProductImagesService  productImagesService;
    @PutMapping("productImage/{id}/{productId}")
    public ResponseEntity updateProductImage(@PathVariable Long id, @PathVariable Long productId, @RequestBody UpdateProductImagesRequest updateProductImagesRequest) {
           productImagesService.updateProductImage(id,productId,updateProductImagesRequest);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("message","Updated product image successfully");
        return ResponseEntity.ok(map);
    }
}
