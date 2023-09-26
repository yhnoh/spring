package com.example.springcloudstreamorder;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.example.springcloudstreamorder.CacheConfig.GOODS_CACHE_NAME;

@FeignClient(name = "goodsOpenFeign", url = "http://localhost:8080", path = "goods")
public interface GoodsOpenFeign {

    @Cacheable(cacheManager = "cacheManager", cacheNames = GOODS_CACHE_NAME, key = "'all'")
    @GetMapping
    List<Goods> getGoodsList();

    @Cacheable(cacheManager = "cacheManager", cacheNames = GOODS_CACHE_NAME, key = "#goodsId")
    @GetMapping("/{id}")
    Goods getGoodsById(@PathVariable("id") long goodsId);

    @PutMapping("/{id}")
    Goods updateGoods(@PathVariable("id") long goodsId, @RequestBody Goods goods);

}
