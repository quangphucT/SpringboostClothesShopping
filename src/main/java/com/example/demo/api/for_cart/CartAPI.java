package com.example.demo.api.for_cart;

import com.example.demo.entity.for_order.Cart;
import com.example.demo.model.for_cart.CartItemRequest;
import com.example.demo.model.for_cart.CartResponse;
import com.example.demo.service.for_cart.CartService;
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
public class CartAPI {
    @Autowired
    CartService cartService;
   @PostMapping("addToCart")
    public ResponseEntity addToCart(@RequestBody CartItemRequest cartItemRequest){
    cartService.addProductToCart(cartItemRequest);
       Map<String,Object> map = new LinkedHashMap<>();
       map.put("message","Added Successfully");
       return ResponseEntity.ok(map);
   }
   @GetMapping("getCartMe")
    public ResponseEntity getCartMe(){
       CartResponse cartme = cartService.getCartMe();
       Map<String,Object> map = new LinkedHashMap<>();
       map.put("message",cartme);
       return ResponseEntity.ok(map);
   }
}
