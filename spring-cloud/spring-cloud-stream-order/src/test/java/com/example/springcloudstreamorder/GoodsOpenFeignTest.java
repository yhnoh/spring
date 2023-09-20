package com.example.springcloudstreamorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GoodsOpenFeignTest {
    @Autowired
    private GoodsOpenFeign goodsOpenFeign;

    @org.junit.jupiter.api.Test
    void test(){
        List<Goods> goods = goodsOpenFeign.getGoodsList();
        System.out.println(goods);
    }

}
