package com.example.springcloudstreamorder;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(path = "/orders")
    public List<OrderGoods> orderGoods(){
        return orderService.orderGoodsList();
    }


    @PostMapping(path = "/orders")
    public OrderGoods orderGoods(@RequestBody OrderRequest orderRequest){
        return orderService.order(orderRequest);
    }
}
