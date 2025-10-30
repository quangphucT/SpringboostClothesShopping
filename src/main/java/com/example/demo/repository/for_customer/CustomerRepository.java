package com.example.demo.repository.for_customer;

import com.example.demo.entity.for_account.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerProfile, Integer> {
}
