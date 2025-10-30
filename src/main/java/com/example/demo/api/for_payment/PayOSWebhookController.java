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
    OrderService orderService;

    // Nhận cả POST và GET để PayOS validate URL
    @RequestMapping(value = "/webhook", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> handlePayOSWebhook(@RequestBody(required = false) Map<String, Object> payload) {

        // Nếu POST có payload thì xử lý dữ liệu
        if (payload != null && payload.get("data") instanceof Map) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if (data.get("orderCode") != null && data.get("status") != null) {
                try {
                    Long orderCode = Long.valueOf(data.get("orderCode").toString());
                    String status = data.get("status").toString();
                    orderService.updateOrderStatus(orderCode, status);
                } catch (NumberFormatException e) {
                    // log lỗi nhưng vẫn trả 200 để PayOS không bị lỗi
                    System.out.println("Invalid orderCode: " + data.get("orderCode"));
                }
            }
        }

        // Luôn trả 200 OK để PayOS dashboard chấp nhận URL
        return ResponseEntity.ok("Webhook received successfully");
    }
}