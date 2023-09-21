package com.example.springcloudstreamorder;

import org.springframework.stereotype.Service;

@Service
public class OrderGoodsMapper {

    public OrderGoods toOrder(OrderEntity orderEntity, Goods goods){
        OrderGoods order = OrderGoods.builder()
                .id(orderEntity.getId())
                .name(orderEntity.getName())
                .quantity(orderEntity.getQuantity())
                .amount(orderEntity.getAmount())
                .totalAmount(orderEntity.getTotalAmount())
                .goods(goods)
                .build();
        return order;
    }
}
