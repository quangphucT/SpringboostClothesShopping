package com.example.demo.api.for_product;

import com.example.demo.entity.for_product.Size;
import com.example.demo.model.for_product.CreateSizeRequest;
import com.example.demo.service.for_product.SizeService;

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
public class SizeAPI {
    @Autowired
    SizeService sizeService;
    @PostMapping("size")
    public ResponseEntity createNewSize(@Valid @RequestBody CreateSizeRequest createSizeRequest) {
        sizeService.createNewSize(createSizeRequest);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("message","Size created successfully!");
        return ResponseEntity.ok(map);
    }
    @GetMapping("sizes")
    public ResponseEntity getAllSize() {
        List<Size> sizes = sizeService.getAllSize();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("size",sizes);
        return ResponseEntity.ok(map);
    }
    @GetMapping("size/{id}")
    public ResponseEntity getSizeById(@PathVariable Long id) {
        Size size = sizeService.findSizeById(id);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("size",size);
        return ResponseEntity.ok(map);
    }
    @PutMapping("size/{id}")
    public ResponseEntity updateSize(@PathVariable Long id, @Valid @RequestBody String name) {
           sizeService.updateSize(id,name);
           Map<String,Object> map = new LinkedHashMap<>();
           map.put("message","Size updated successfully!");
           return ResponseEntity.ok(map);
    }
    @DeleteMapping("size/{id}")
    public ResponseEntity deleteSize(@PathVariable Long id) {
         sizeService.deleteSize(id);
         Map<String,Object> map = new LinkedHashMap<>();
         map.put("message","Size deleted successfully!");
         return ResponseEntity.ok(map);
    }
}
