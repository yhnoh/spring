package org.example.springbatchitemwriterjpa;

import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.springbatchitemwriterjpa.jpa.OrderJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME;

@Service
@Transactional(transactionManager = JPA_TRANSACTION_MANAGER_BEAN_NAME)
@RequiredArgsConstructor
class OrderService {

    private final OrderJpaRepository orderJpaRepository;

    public OrderJpaEntity save(long memberId) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().memberId(memberId).build();
        return orderJpaRepository.save(orderJpaEntity);
    }

    public List<OrderJpaEntity> findAllByMemberIdIn(List<Long> memberIds) {
        return orderJpaRepository.findAllByMemberIdIn(memberIds);
    }

}
