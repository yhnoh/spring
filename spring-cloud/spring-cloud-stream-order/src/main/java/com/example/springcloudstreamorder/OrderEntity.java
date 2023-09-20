package com.example.springcloudstreamorder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "goodsId")
    private long goodsId;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "amount")
    private int amount;

    @Builder
    public OrderEntity(long goodsId, String name, int quantity, int amount) {
        this.goodsId = goodsId;
        this.name = name;
        this.quantity = quantity;
        this.amount = amount;
    }
}
