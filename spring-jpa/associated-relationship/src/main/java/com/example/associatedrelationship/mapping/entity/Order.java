package com.example.associatedrelationship.mapping.entity;

import com.example.associatedrelationship.NotFoundDataException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private String name;
    private double price;
    private double discountPrice;
    private double orderPrice;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private OrderUserInfo orderUserInfo;

    @OneToMany(mappedBy = "order")
    List<GoodsOrder> goodsOrder = new ArrayList<>();

    public static Order createOrder(List<GoodsOrder> goodsOrders){
        Order order = new Order();
        order.name = goodsOrders.stream().map(GoodsOrder::getGoodsName).collect(Collectors.joining("/"));
        order.price = goodsOrders.stream().mapToDouble(GoodsOrder::getPrice).sum();
        order.discountPrice =  goodsOrders.stream().mapToDouble(GoodsOrder::getDiscountPrice).sum();
        order.orderPrice =  goodsOrders.stream().mapToDouble(GoodsOrder::getOrderPrice).sum();
        order.goodsOrder = goodsOrders;
        for (GoodsOrder goodsOrder : goodsOrders) {
            goodsOrder.setOrder(order);
        }
        return order;
    }





}
