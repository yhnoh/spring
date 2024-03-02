package org.example.lock.concurrnecy_insert_issue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderJpaRepository orderJpaRepository;

    public List<OrderJpaEntity> getOrders(){
        return orderJpaRepository.findAll();
    }

    public void order(String orderNum){
        OrderJpaEntity orderJpaEntity = new OrderJpaEntity(orderNum);
        orderJpaRepository.save(orderJpaEntity);
    }
}
