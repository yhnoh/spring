package com.example.associatedrelationship.mapping.repository;

import com.example.associatedrelationship.mapping.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
