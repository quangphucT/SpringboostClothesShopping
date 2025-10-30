package com.example.demo.service.for_product;

import com.example.demo.entity.for_product.*;
import com.example.demo.entity.for_productVariant.ProductVariant;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.for_product.CreateNewProductRequest;
import com.example.demo.model.for_product.UpdateProductRequest;
import com.example.demo.model.for_productVariant.ProductVariantRequest;
import com.example.demo.repository.for_product.CategoryRepository;
import com.example.demo.repository.for_product.ColorRepository;
import com.example.demo.repository.for_product.ProductRepository;
import com.example.demo.repository.for_product.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    SizeRepository sizeRepository;

    public void createNewProduct(CreateNewProductRequest createNewProductRequest){
             Product product = new Product();
             product.setName(createNewProductRequest.getName());
             product.setDescription(createNewProductRequest.getDescription());
             product.setThumbnail(createNewProductRequest.getThumbnail());
             product.setCreated_at(new Date());
             product.setUpdated_at(new Date());
             Category category = categoryRepository.findById(createNewProductRequest.getCategoryId())
                     .orElseThrow(() -> new NotFoundException("Category not found") {
                     });
             product.setCategory(category);

             List<ProductionImage> productionImages = new ArrayList<>();
             for(String subImage : createNewProductRequest.getProductionImages()){
                 ProductionImage productionImage = new ProductionImage();
                 productionImage.setProduct(product);
                 productionImage.setImageUrl(subImage);
                 productionImages.add(productionImage);
             }
             product.setProduction_images(productionImages);

             List<ProductVariant> productVariants = new ArrayList<>();
             for(ProductVariantRequest productVariantRequest : createNewProductRequest.getProductVariantRequests()){
                 ProductVariant productVariant = new ProductVariant();
                 productVariant.setProduct(product);
                 productVariant.setStockQuantity(productVariantRequest.getStockQuantity());
                 productVariant.setPrice(productVariantRequest.getPrice());

                 Color color = colorRepository.findById(productVariantRequest.getColorId())
                         .orElseThrow(() -> new NotFoundException("Color not found") {
                         });
                 productVariant.setColor(color);

                 Size size = sizeRepository.findById(productVariantRequest.getSizeId())
                         .orElseThrow(() -> new NotFoundException("Size not found") {
                         });
                 productVariant.setSize(size);
                 productVariants.add(productVariant);
             }
             product.setProductVariants(productVariants);

             productRepository.save(product);

    }
    public List<Product> getAllProducts(){
            List<Product> products = productRepository.findAllByIsDeletedIsFalse();
            return products;
    }
    public Product getProductById(Long id){
           Product product = productRepository.findProductById(id);
           if(product == null){
               throw new NotFoundException("Product not found");
           }else{
               return product;
           }
    }
    public void deleteProductById(Long id){
        Product product = productRepository.findProductById(id);
        if(product == null){
            throw new NotFoundException("Product not found");
        }else{
            product.setIsDeleted(true);
            productRepository.save(product);
        }
    }

    public void updateProductById(Long id, UpdateProductRequest updateProductRequest){
        Product product = productRepository.findProductById(id);
        if(product == null){
            throw new NotFoundException("Product not found");
        }else{
            product.setName(updateProductRequest.getName());
            product.setDescription(updateProductRequest.getDescription());
            product.setThumbnail(updateProductRequest.getThumbnail());
            product.setUpdated_at(new Date());
            Category category = categoryRepository.findById(updateProductRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found") {
                    });
            product.setCategory(category);
            productRepository.save(product);
        }
    }

}
