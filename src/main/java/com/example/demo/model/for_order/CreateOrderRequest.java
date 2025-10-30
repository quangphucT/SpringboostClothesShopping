package com.example.demo.model.for_order;

import com.example.demo.entity.for_payment.PaymentMethod;
import lombok.Data;

@Data
public class CreateOrderRequest {
     PaymentMethod paymentMethod;
     String shippingAddress;
     String note;
}
