package com.example.mapstruct.defining_a_mapper.repository;

import com.example.mapstruct.defining_a_mapper.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
