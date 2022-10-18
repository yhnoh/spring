package com.example.associatedrelationship.mapping.entity;


import com.example.associatedrelationship.NotFoundDataException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_order_id")
    private Long id;
    private String goodsName;
    private int orderQuantity;
    private double price;
    private double discountPrice;
    private double orderPrice;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public static GoodsOrder createGoodsOrder(Goods goods, int orderQuantity){
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.goodsName = goods.getName();
        goodsOrder.orderQuantity = orderQuantity;
        goods.minusTotalQuantity(orderQuantity);
        goodsOrder.price = goods.getPrice() * orderQuantity;
        goodsOrder.discountPrice = goods.getDiscountPrice() * orderQuantity;
        goodsOrder.orderPrice = goods.getOrderPrice() * orderQuantity;
        goods.addGoodsOrder(goodsOrder);
        return goodsOrder;
    }

    public void setOrder(Order order){
        if(order == null){
            throw new NotFoundDataException("order is null when setOrder");
        }
        this.order = order;
    }
}
