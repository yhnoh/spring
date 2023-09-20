package com.example.springcloudstreamgoods;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "goods")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoodsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private int quantity;

    @Builder
    public GoodsEntity(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void changeByGoods(Goods goods){
        this.name = goods.getName();
        this.quantity = goods.getQuantity();
    }
}
