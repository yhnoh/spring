package org.example.springbatchmultidatasource;

import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.example.springbatchmultidatasource.jpa.OrderJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME;
import org.example.springbatchmultidatasource.jpa.order.OrderJpaEntity;
import org.example.springbatchmultidatasource.jpa.order.OrderJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
