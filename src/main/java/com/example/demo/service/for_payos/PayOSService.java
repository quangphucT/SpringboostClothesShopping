package com.example.demo.service.for_payos;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;

@Service
public class PayOSService {

    private final PayOS payOS;

    public PayOSService(PayOS payOS) {
        this.payOS = payOS;
    }

    /**
     * Tạo link thanh toán QR thật từ PayOS
     */
    public CreatePaymentLinkResponse createPaymentLink(
            long orderId,
            double amount,
            String description
    ) throws Exception {

        long roundedAmount = (long) Math.round(amount);

        PaymentLinkItem item = PaymentLinkItem.builder()
                .name("Order #" + orderId)
                .price(roundedAmount)
                .quantity(1)
                .build();

        CreatePaymentLinkRequest req = CreatePaymentLinkRequest.builder()
                .orderCode(orderId)
                .amount(roundedAmount)
                .description(description)
                .item(item)
                .returnUrl("https://your-frontend.com/payment-success")
                .cancelUrl("https://your-frontend.com/payment-cancel")
                .build();

        return payOS.paymentRequests().create(req);
    }
}