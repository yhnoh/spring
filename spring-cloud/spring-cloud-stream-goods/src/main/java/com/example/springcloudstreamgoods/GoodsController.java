package com.example.springcloudstreamgoods;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GoodsController {

    private final GoodsService goodsService;
    @GetMapping("/goods")
    public List<Goods> goodsList(){
        return goodsService.getGoodsList();
    }

    @GetMapping("/goods/{id}")
    public Goods goods(@PathVariable("id") long id){
        return goodsService.getGoodsById(id);
    }

    @PostMapping("/goods")
    public Goods saveGoods(@RequestBody Goods goods){
        return goodsService.saveGoods(goods);
    }
    @PutMapping("/goods/{id}")
    public Goods updateGoods(@PathVariable("id") long id, @RequestBody Goods goods){
        return goodsService.updateGoods(id, goods);
    }

}
