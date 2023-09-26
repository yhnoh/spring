package com.example.springcloudstreamorder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class GoodsOpenFeignTest {
    @Autowired
    private GoodsOpenFeign goodsOpenFeign;

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void test(){
        List<Goods> goods = goodsOpenFeign.getGoodsList();
        System.out.println(goods);
    }

    @Autowired
    private CacheManager cacheManager;
    @Test
    void test2(){
        Collection<String> cacheNames = cacheManager.getCacheNames();


    }
}
