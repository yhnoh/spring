package com.example.associatedrelationship.mapping.repository;

import com.example.associatedrelationship.mapping.entity.GoodsOrder;
import com.example.associatedrelationship.mapping.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsOrderJpaRepository extends JpaRepository<GoodsOrder, Long> {
}
