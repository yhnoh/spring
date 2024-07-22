package org.example.springbatchitemwriterjpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.example.springbatchitemwriterjpa.jpa.OrderJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME;
import org.example.springbatchitemwriterjpa.jpa.order.OrderItemJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderItemJpaRepository;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = JPA_TRANSACTION_MANAGER_BEAN_NAME)
@RequiredArgsConstructor
class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;


    public OrderJpaEntity save(long memberId) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().memberId(memberId).build();
        orderJpaRepository.save(orderJpaEntity);

        OrderItemJpaEntity orderItemJpaEntity = OrderItemJpaEntity.builder().order(orderJpaEntity).build();
        orderItemJpaRepository.save(orderItemJpaEntity);

        return orderJpaEntity;
    }


    public List<OrderJpaEntity> findAll() {
        return orderJpaRepository.findAll();
    }
}
