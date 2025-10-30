package com.example.demo.entity.for_account;

import com.example.demo.entity.for_order.Cart;
import com.example.demo.entity.for_order.OrderOfCustomer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class CustomerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Enumerated(EnumType.STRING)
    Gender gender;
    Date dateOfBirth;
    String address;
    String city;
    String country;
    String avatarUrl;
    Date createdAt;
    Date updatedAt;

    @OneToOne
    @JoinColumn(name = "account_id")
    Account account;

    @OneToOne(mappedBy = "customerProfile",  cascade = CascadeType.ALL)
    Cart cart;

    @OneToMany(mappedBy = "customerProfile")
    List<OrderOfCustomer> orderOfCustomers;

}
