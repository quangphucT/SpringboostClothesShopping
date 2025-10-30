package com.example.demo.service.for_product;

import com.example.demo.entity.for_product.Category;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.for_category.CreateCategoryRequest;
import com.example.demo.repository.for_product.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public void createNewCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category();
        category.setName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());
        category.setCreated_at(new Date());
        category.setUpdated_at(new Date());
        categoryRepository.save(category);
    }
    public void  updateCategory(CreateCategoryRequest createCategoryRequest, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found") {
                });
       category.setName(createCategoryRequest.getName());
       category.setDescription(createCategoryRequest.getDescription());
       category.setUpdated_at(new Date());
       categoryRepository.save(category);
    }
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found") {
                });

        category.setDeleted(true);
        categoryRepository.save(category);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByIsDeletedFalse();
    }
    public Category getCategoryDetails(Long id){
        return  categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found") {});
    }
}
