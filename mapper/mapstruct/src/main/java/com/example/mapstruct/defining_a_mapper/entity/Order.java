package com.example.mapstruct.defining_a_mapper.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderName;
    private LocalDateTime createdDatetime;
    private Integer quantity;

    @ToString.Exclude
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public static Order createOrder(String orderName, int quantity, Member member) {
        Order order = new Order();
        order.orderName = orderName;
        order.createdDatetime = LocalDateTime.now();
        order.quantity = quantity;
        order.member = member;
        order.member.addOrder(order);
        return order;
    }

}
