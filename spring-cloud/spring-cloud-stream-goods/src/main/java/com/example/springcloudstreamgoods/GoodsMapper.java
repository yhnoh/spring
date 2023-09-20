package com.example.springcloudstreamgoods;

import org.springframework.stereotype.Service;

@Service
public class GoodsMapper {

    public Goods toGoods(GoodsEntity goodsEntity){
        return Goods.builder().id(goodsEntity.getId())
                .name(goodsEntity.getName())
                .quantity(goodsEntity.getQuantity())
                .build();
    }

    public GoodsEntity toGoodEntity(Goods goods){
        return GoodsEntity.builder().name(goods.getName()).quantity(goods.getQuantity()).build();
    }
}
