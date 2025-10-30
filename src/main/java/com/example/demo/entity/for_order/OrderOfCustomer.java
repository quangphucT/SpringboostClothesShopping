package com.example.demo.entity.for_order;

import com.example.demo.entity.for_payment.Payment;
import com.example.demo.entity.for_payment.PaymentMethod;
import com.example.demo.entity.for_account.CustomerProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderOfCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Date orderDate; //
    Double total_price; //
    PaymentMethod payment_method; //
    String shipping_address; //
    String note; //
    Date created_at; //

    @ManyToOne
    @JoinColumn(name = "customerProfile_id")
    CustomerProfile customerProfile; //


    @OneToMany(mappedBy = "orderOfCustomer", cascade = CascadeType.ALL)
            @JsonIgnore
    List<OrderItem> order_items; //


    @OneToOne(mappedBy = "orderOfCustomer")
    Payment payment;
}
