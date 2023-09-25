package com.example.springcloudstreamorder;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(path = "/orders")
    public List<OrderGoods> orderGoods(){
        return orderService.orderGoodsList();
    }

    @GetMapping(path = "/orders/{id}")
    public OrderGoods orderGoods(@PathVariable long id){
        return orderService.getOrderGoods(id);
    }


    @PostMapping(path = "/orders")
    public OrderGoods orderGoods(@RequestBody OrderRequest orderRequest){
        return orderService.order(orderRequest);
    }

}
