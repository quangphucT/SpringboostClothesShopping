package com.example.demo.api.for_product;


import com.example.demo.entity.for_product.Product;
import com.example.demo.model.for_product.CreateNewProductRequest;
import com.example.demo.model.for_product.UpdateProductRequest;
import com.example.demo.service.for_product.ProductService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ProductAPI {
    @Autowired
    ProductService productService;
    @PostMapping("product")
    public ResponseEntity createNewProduct(@Valid  @RequestBody CreateNewProductRequest createNewProductRequest) {
            productService.createNewProduct(createNewProductRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Product created successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("products")
    public ResponseEntity getAllProducts() {
        List products  =productService.getAllProducts();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", products);
        return ResponseEntity.ok(products);
    }
    @GetMapping("product/{id}")
    public ResponseEntity getProductById(@PathVariable Long id) {
          Product product = productService.getProductById(id);
          Map<String, Object> response = new LinkedHashMap<>();
          response.put("data", product);
          return ResponseEntity.ok(response);
    }
    @DeleteMapping("product/{id}")
    public ResponseEntity deleteProductById(@PathVariable Long id) {
          productService.deleteProductById(id);
          Map<String, Object> response = new LinkedHashMap<>();
          response.put("message", "Product deleted successfully");
          return ResponseEntity.ok(response);
    }
    @PutMapping("productBasicInformation/{id}")
    public ResponseEntity updateProductById(@PathVariable Long id, @Valid  @RequestBody UpdateProductRequest updateProductRequest) {
        productService.updateProductById(id, updateProductRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Product updated successfully");
        return ResponseEntity.ok(response);
    }
}
