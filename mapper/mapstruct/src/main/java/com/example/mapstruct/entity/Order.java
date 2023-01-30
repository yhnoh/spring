package com.example.mapstruct.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderName;
    private LocalDateTime createdDatetime;
    private Integer quantity;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public static Order createOrder(String orderName, int quantity, Member member) {
        Order order = new Order();
        order.orderName = orderName;
        order.createdDatetime = LocalDateTime.now();
        order.quantity = quantity;
        order.member = member;
        return order;
    }


}
