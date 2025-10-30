package com.example.demo.api.for_payment;
import com.example.demo.service.for_order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payos")
public class PayOSWebhookController {
     @Autowired
    OrderService  orderService;
    @PostMapping("/webhook")
    public ResponseEntity<String> handlePayOSWebhook(@RequestBody(required = false) Map<String, Object> payload) {

        // Lấy dữ liệu từ payload
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if (data != null) {
            Long orderCode = Long.valueOf(data.get("orderCode").toString());
            String status = (String) data.get("status");
            orderService.updateOrderStatus(orderCode, status);
        }
        return ResponseEntity.ok("Webhook received successfully");
    }
}
