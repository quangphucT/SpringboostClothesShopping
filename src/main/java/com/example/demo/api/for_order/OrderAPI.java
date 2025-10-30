package com.example.demo.api.for_order;

import com.example.demo.model.for_order.CreateOrderRequest;
import com.example.demo.model.for_order.OrderResponse;
import com.example.demo.service.for_order.OrderService;
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
public class OrderAPI {

    @Autowired
    private OrderService orderService;

    @PostMapping("order")
    public ResponseEntity<?> orderProduct(@RequestBody CreateOrderRequest createOrderRequest) {
        OrderResponse orderResponse = orderService.createOrder(createOrderRequest);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", "Order Created");
        map.put("orderResponse", orderResponse);

        return ResponseEntity.ok(map);
    }
}