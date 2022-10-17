package com.example.associatedrelationship.mapping.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_order_id")
    private Long id;
    private String name;
    private double price;
    private double discountPrice;
    private double orderPrice;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;

    public static Order createGoodsOrder(Goods goods, int quantity){
        Order goodsOrder = new Order();
        goodsOrder.name = goods.getName();
        goods.minusTotalQuantity(quantity);
        goodsOrder.quantity = quantity;
        goodsOrder.price = goods.getPrice() * quantity;
        goodsOrder.discountPrice = goods.getDiscountPrice() * quantity;
        goodsOrder.orderPrice = goods.getOrderPrice() * quantity;
        goodsOrder.goods = goods;
        goods.addGoodsOrder(goodsOrder);
        return goodsOrder;
    }


}
