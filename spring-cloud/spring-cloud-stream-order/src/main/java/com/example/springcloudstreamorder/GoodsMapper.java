package com.example.springcloudstreamorder;

import org.springframework.stereotype.Service;

@Service
public class GoodsMapper {

    public GoodsRedisEntity toGoodsRedisEntity(Goods goods){
        return GoodsRedisEntity.builder()
                .id(goods.getId())
                .name(goods.getName())
                .quantity(goods.getQuantity())
                .amount(goods.getAmount())
                .build();
    }
}
