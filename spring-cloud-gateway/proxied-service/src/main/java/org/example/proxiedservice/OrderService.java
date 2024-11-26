package org.example.proxiedservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private Map<Long, Order> orders;

    @PostConstruct
    public void init(){
        Order order1 = Order.builder().id(1).name("주문된 상품1").state("주문완료").build();
        Order order2 = Order.builder().id(2).name("주문된 상품2").state("주문완료").build();

        orders = Map.of(order1.getId(), order1, order2.getId(), order2);
    }

    public List<Order> getOrders(){
        return new ArrayList<>(orders.values());
    }


    public Order getOrder(long id){
        return orders.get(id);
    }
}
