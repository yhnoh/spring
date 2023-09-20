package com.example.springcloudstreamorder;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Goods {

    private long id;
    private String name;
    private int quantity;

    public void changeQuantity(int orderQuantity){
        if(orderQuantity > quantity){
            throw new IllegalArgumentException("상품 재고가 부족합니다.");
        }
    }
}