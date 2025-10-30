package com.example.demo.api.for_product;

import com.example.demo.entity.for_product.Color;
import com.example.demo.model.for_product.CreateColorRequest;
import com.example.demo.service.for_product.ColorService;
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
public class ColorAPI {
    @Autowired
    ColorService colorService;
    @PostMapping("color")
    public ResponseEntity createNewColor(@Valid @RequestBody CreateColorRequest createColorRequest) {
             colorService.createNewColor(createColorRequest);
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("message", "Created new color successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("colors")
    public ResponseEntity getAllColors(){
        List<Color> colors = colorService.getAllColors();
        Map<String,Object> response = new LinkedHashMap<>();
        response.put("colors", colors);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("color/{id}")
    public ResponseEntity deleteColor(@PathVariable Long id){
             colorService.deletedColors(id);
             Map<String,Object> response = new LinkedHashMap<>();
             response.put("message", "Deleted color successfully");
             return ResponseEntity.ok(response);
    }
    @PutMapping("color/{id}")
    public ResponseEntity updateColor(@PathVariable Long id, @Valid @RequestBody String name){
          colorService.updateColor(id,name);
          Map<String,Object> response = new LinkedHashMap<>();
          response.put("message", "Updated color successfully");
          return ResponseEntity.ok(response);
    }
    @GetMapping("color/{id}")
    public ResponseEntity getColorById(@PathVariable Long id){
          Color color =  colorService.getColorById(id);
            Map<String,Object> response = new LinkedHashMap<>();
            response.put("color", color);
            return ResponseEntity.ok(response);
    }
}
