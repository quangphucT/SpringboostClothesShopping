package com.example.demo.api.for_product;

import com.example.demo.entity.for_product.Category;
import com.example.demo.model.for_category.CreateCategoryRequest;
import com.example.demo.service.for_product.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class CategoryAPI {
    @Autowired
    CategoryService categoryService;
    @PostMapping("category")
    public ResponseEntity createNewCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
            categoryService.createNewCategory(createCategoryRequest);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("message","Category created successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("category/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody CreateCategoryRequest createCategoryRequest) {
       categoryService.updateCategory(createCategoryRequest,id);
       Map<String,Object> response = new LinkedHashMap<>();
       response.put("message","Category updated successfully");
       return ResponseEntity.ok(response);
    }
    @DeleteMapping("category/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("message","Category deleted successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("categories")
    public ResponseEntity getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("data",categories);
        return ResponseEntity.ok(response);
    }
    @GetMapping("category/{id}")
    public ResponseEntity getCategoryDetails(@PathVariable Long id) {
        Category category = categoryService.getCategoryDetails(id);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("data",category);
        return ResponseEntity.ok(response);
    }
}
