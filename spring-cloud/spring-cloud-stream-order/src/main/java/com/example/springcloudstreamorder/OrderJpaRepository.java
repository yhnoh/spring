package com.example.springcloudstreamorder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
