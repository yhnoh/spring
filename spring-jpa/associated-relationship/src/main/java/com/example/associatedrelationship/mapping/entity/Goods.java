package com.example.associatedrelationship.mapping.entity;

import com.example.associatedrelationship.NotFoundDataException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long id;
    private String name;
    private int totalQuantity;
    private int remainQuantity;
    private double price;
    private double discountPrice;

    @OneToMany(mappedBy = "goods")
    private List<Order> goodsOrders = new ArrayList<>();

    public static Goods createGoods(String name, int totalQuantity, double price, double discountPrice){
        Goods goods = new Goods();
        goods.name = name;
        goods.totalQuantity = totalQuantity;
        goods.remainQuantity = totalQuantity;
        goods.price = price;
        goods.discountPrice = discountPrice;
        return goods;
    }

    public void addGoodsOrder(Order goodsOrder){
        if(goodsOrder == null){
            throw new NotFoundDataException("goodsOrder is null when call addGoodsOrder");
        }
        goodsOrders.add(goodsOrder);
    }
    public void minusTotalQuantity(int quantity){
        if(quantity > remainQuantity){
            throw new NotFoundDataException("this quantity exceed remainQuantity");
        }
        remainQuantity -= quantity;
    }

    public double getOrderPrice(){
        return price - discountPrice;
    }
}
