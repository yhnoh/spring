package com.example.springcloudstreamorder;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/order")
    public OrderRequest orderGoods(@RequestBody OrderRequest){

    }
}
