package com.example.springbatchdatabase.repository;

import com.example.springbatchdatabase.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
