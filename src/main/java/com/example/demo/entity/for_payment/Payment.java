package com.example.demo.entity.for_payment;

import com.example.demo.entity.for_order.OrderOfCustomer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Double amount;
    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;
    Date payment_date;

    @OneToOne
    @JoinColumn(name = "orderOfCustomer_id")
    OrderOfCustomer orderOfCustomer;
    String paymentUrl;
}
