package com.example.demo.api.for_productVariant;

import com.example.demo.model.for_productVariant.UpdateProductVariantRequest;
import com.example.demo.service.for_productVariant.ProductVariantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ProductVariantAPI {
    @Autowired
    ProductVariantService productVariantService;
    @PutMapping("productVariant/{id}/{productId}")
    public ResponseEntity updateProductVariant(@PathVariable Long id, @PathVariable Long productId, @RequestBody UpdateProductVariantRequest updateProductVariantRequest){
               productVariantService.updateProductVariant(id, productId, updateProductVariantRequest);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("message","Updated product variant successfully");
        return ResponseEntity.ok(map);
    }

}
