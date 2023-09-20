package com.example.springcloudstreamorder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "goodsOpenFeign", url = "http://localhost:8080", path = "goods")
public interface GoodsOpenFeign {

    @GetMapping
    List<Goods> getGoodsList();

    @GetMapping("/{id}")
    Goods getGoodsById(@PathVariable("id") long goodsId);

    @PutMapping("/{id}")
    Goods updateGoods(@PathVariable("id") long goodsId, @RequestBody Goods goods);

}
