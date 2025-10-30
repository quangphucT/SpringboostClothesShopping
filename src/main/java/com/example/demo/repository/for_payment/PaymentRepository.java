package com.example.demo.repository.for_payment;

import com.example.demo.entity.for_payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderOfCustomerId(Long orderOfCustomerId);
}
