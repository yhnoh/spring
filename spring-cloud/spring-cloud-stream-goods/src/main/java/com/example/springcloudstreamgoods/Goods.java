package com.example.springcloudstreamgoods;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class Goods {

    private long id;
    private String name;
    private int quantity;
    private int amount;
}
