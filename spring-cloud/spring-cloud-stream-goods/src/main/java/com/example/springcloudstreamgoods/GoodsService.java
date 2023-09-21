package com.example.springcloudstreamgoods;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsService {

    private final GoodsMapper goodsMapper;
    private final GoodsJpaRepository goodsJpaRepository;


    public List<Goods> getGoodsList(){
        return goodsJpaRepository.findAll().stream()
                .map(goodsMapper::toGoods)
                .collect(Collectors.toList());
    }

    public Goods getGoodsById(long id){
        return goodsJpaRepository.findById(id).map(goodsMapper::toGoods).orElse(null);
    }

    public Goods saveGoods(GoodsRegistrationRequest registrationRequest){

        GoodsEntity goodEntity = goodsMapper.toGoodEntity(registrationRequest);
        goodsJpaRepository.save(goodEntity);
        return goodsMapper.toGoods(goodEntity);
    }

    public Goods updateGoods(long id, GoodsRegistrationRequest registrationRequest){
        GoodsEntity goodsEntity = goodsJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다."));
        Goods goods = goodsMapper.toGoods(registrationRequest);
        goodsEntity.changeByGoods(goods);
        return goodsMapper.toGoods(goodsEntity);
    }
}
