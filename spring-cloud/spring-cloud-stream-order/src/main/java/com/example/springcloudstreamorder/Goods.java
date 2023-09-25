package com.example.springcloudstreamorder;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Goods {

    private long id;
    private String name;
    private int quantity;
    private int amount;
    public void changeQuantity(int orderQuantity){
        if(orderQuantity > quantity){
            throw new IllegalArgumentException("상품 재고가 부족합니다.");
        }
        this.quantity -= orderQuantity;
    }

    Goods() {
    }

    @Builder
    public Goods(long id, String name, int quantity, int amount) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }
}