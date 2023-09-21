package com.example.springcloudstreamorder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class GoodsOpenFeignTest {
    @Autowired
    private GoodsOpenFeign goodsOpenFeign;

    @Autowired
    private GoodsRedisRepository goodsRedisRepository;
    @Autowired
    private GoodsMapper goodsMapper;
    @Test
    void test(){
        List<Goods> goods = goodsOpenFeign.getGoodsList();
        System.out.println(goods);
    }

    @Test
    void redisTest(){
        List<GoodsRedisEntity> goodsRedisEntities = goodsOpenFeign.getGoodsList().stream().map(goodsMapper::toGoodsRedisEntity).collect(Collectors.toList());

        Iterable<GoodsRedisEntity> goodsRedisEntities1 = goodsRedisRepository.saveAll(goodsRedisEntities);


    }
}
