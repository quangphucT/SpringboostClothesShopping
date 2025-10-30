package com.example.demo.repository.for_order;

import com.example.demo.entity.for_order.OrderOfCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOfCustomerRepository extends JpaRepository<OrderOfCustomer,Long> {
}
