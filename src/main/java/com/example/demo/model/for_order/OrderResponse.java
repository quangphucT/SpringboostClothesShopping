package com.example.demo.model.for_order;

import com.example.demo.entity.for_payment.PaymentMethod;
import lombok.Data;

import java.util.Date;

@Data
public class OrderResponse {
    Long orderId;
    Double totalPrice;
    PaymentMethod paymentMethod;
    String shippingAddress;
    String note;
    Date orderDate;
    String qrLinkPayment;
}
