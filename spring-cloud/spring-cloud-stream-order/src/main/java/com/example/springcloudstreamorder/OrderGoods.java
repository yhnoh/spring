package com.example.springcloudstreamorder;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderGoods {

    private long id;
    private String name;
    private int quantity;
    private int amount;
    private int totalAmount;
    private Goods goods;
}
