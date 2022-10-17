package com.example.associatedrelationship.mapping.repository;

import com.example.associatedrelationship.mapping.entity.OrderUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderUserInfoJpaRepository extends JpaRepository<OrderUserInfo, Long> {
}
