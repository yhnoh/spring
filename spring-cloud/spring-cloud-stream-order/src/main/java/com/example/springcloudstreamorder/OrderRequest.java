package com.example.springcloudstreamorder;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderRequest {

    private long goodsId;
    private int quantity;

}
