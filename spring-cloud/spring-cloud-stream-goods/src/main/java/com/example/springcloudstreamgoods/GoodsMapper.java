package com.example.springcloudstreamgoods;

import org.springframework.stereotype.Service;

@Service
public class GoodsMapper {

    public Goods toGoods(GoodsEntity goodsEntity){
        return Goods.builder().id(goodsEntity.getId())
                .name(goodsEntity.getName())
                .quantity(goodsEntity.getQuantity())
                .amount(goodsEntity.getAmount())
                .build();
    }

    public Goods toGoods(GoodsRegistrationRequest goodsRegistrationRequest){
        return Goods.builder()
                .name(goodsRegistrationRequest.getName())
                .quantity(goodsRegistrationRequest.getQuantity())
                .amount(goodsRegistrationRequest.getAmount())
                .build();
    }

    public GoodsEntity toGoodEntity(GoodsRegistrationRequest goodsRegistrationRequest){
        return GoodsEntity.builder()
                .name(goodsRegistrationRequest.getName())
                .quantity(goodsRegistrationRequest.getQuantity())
                .amount(goodsRegistrationRequest.getAmount()).build();
    }


    public GoodsEntity toGoodEntity(Goods goods){
        return GoodsEntity.builder().name(goods.getName()).quantity(goods.getQuantity()).build();
    }
}
