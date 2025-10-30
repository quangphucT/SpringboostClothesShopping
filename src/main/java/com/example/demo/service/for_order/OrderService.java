package com.example.demo.service.for_order;

import com.example.demo.entity.for_account.CustomerProfile;
import com.example.demo.entity.for_order.Cart;
import com.example.demo.entity.for_order.CartItem;
import com.example.demo.entity.for_order.OrderItem;
import com.example.demo.entity.for_order.OrderOfCustomer;
import com.example.demo.entity.for_payment.Payment;
import com.example.demo.entity.for_payment.PaymentMethod;
import com.example.demo.entity.for_payment.PaymentStatus;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.for_order.CreateOrderRequest;
import com.example.demo.model.for_order.OrderResponse;
import com.example.demo.repository.for_cart.CartRepository;
import com.example.demo.repository.for_order.OrderOfCustomerRepository;
import com.example.demo.repository.for_payment.PaymentRepository;
import com.example.demo.service.for_authen.AuthenticationService;
import com.example.demo.service.for_payos.PayOSService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderOfCustomerRepository  orderOfCustomerRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PayOSService payOSService;
    public OrderResponse createOrder(CreateOrderRequest createOrderRequest){
       // Lấy thông tin customer đang đăng nhập
        CustomerProfile customerProfile = authenticationService.getCurrentAccount().getCustomerProfile();
        Cart cart = customerProfile.getCart();
        if(cart.getCartItems().isEmpty()){
            throw new NotFoundException("Cart items not found");
        }
        // Tạo mới orderOfCustomer
        OrderOfCustomer orderOfCustomer = new OrderOfCustomer();
        orderOfCustomer.setOrderDate(new Date());
        orderOfCustomer.setCustomerProfile(customerProfile);
        orderOfCustomer.setPayment_method(createOrderRequest.getPaymentMethod());
        orderOfCustomer.setNote(createOrderRequest.getNote());
        orderOfCustomer.setShipping_address(createOrderRequest.getShippingAddress());
        orderOfCustomer.setCreated_at(new Date());

        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cart.getCartItems()){
               OrderItem orderItem = new OrderItem();
               orderItem.setOrderOfCustomer(orderOfCustomer);
               orderItem.setQuantity(cartItem.getQuantity());
               orderItem.setUnitPrice(cartItem.getProductVariant().getPrice());
               orderItem.setProduct(cartItem.getProductVariant().getProduct());
               total += (cartItem.getProductVariant().getPrice() *  cartItem.getQuantity());
               orderItems.add(orderItem);
        }
        orderOfCustomer.setTotal_price(total);
        orderOfCustomer.setOrder_items(orderItems);
        orderOfCustomerRepository.save(orderOfCustomer);

        // tạo payment
        Payment payment = new Payment();
        payment.setOrderOfCustomer(orderOfCustomer);
        payment.setPaymentMethod(createOrderRequest.getPaymentMethod());
        payment.setPayment_date(new Date());
        if(createOrderRequest.getPaymentMethod() == PaymentMethod.CASH){
              payment.setPaymentStatus(PaymentStatus.UNPAID);
        }else if(createOrderRequest.getPaymentMethod() == PaymentMethod.PAYOS){
            payment.setPaymentStatus(PaymentStatus.PENDING);
            try {
                // Gọi PayOS thật
                CreatePaymentLinkResponse response = payOSService.createPaymentLink(
                        orderOfCustomer.getId(),
                        orderOfCustomer.getTotal_price(),
                        "Thanh toán đơn hàng #" + orderOfCustomer.getId()
                );

                payment.setPaymentUrl(response.getQrCode());
                payment.setAmount(total);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi tạo link thanh toán PayOS: " + e.getMessage());
            }
        }
         paymentRepository.save(payment);

        // 5️⃣ Dọn giỏ hàng
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // 6️⃣ Tạo response trả về cho FE
        OrderResponse response = new OrderResponse();
        response.setOrderId(orderOfCustomer.getId());
        response.setTotalPrice(orderOfCustomer.getTotal_price());
        response.setPaymentMethod(createOrderRequest.getPaymentMethod());
        response.setShippingAddress(createOrderRequest.getShippingAddress());
        response.setNote(createOrderRequest.getNote());
        response.setOrderDate(orderOfCustomer.getOrderDate());
        response.setQrLinkPayment(payment.getPaymentUrl());
        return response;
    }
    public void updateOrderStatus(Long orderId, String statusFromWebhook) {
        OrderOfCustomer order = orderOfCustomerRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Payment payment = order.getPayment();
        try {
            // Convert từ chuỗi trong webhook (vd: "PAID") sang enum
            PaymentStatus newStatus = PaymentStatus.valueOf(statusFromWebhook.toUpperCase());
            payment.setPaymentStatus(newStatus);

            orderOfCustomerRepository.save(order);
            System.out.println("✅ Đơn hàng " + orderId + " cập nhật paymentStatus: " + newStatus);
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Webhook gửi status không hợp lệ: " + statusFromWebhook);
        }
    }
}
