package com.example.associatedrelationship.mapping.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_user_info_id")
    private Long id;
    private String username;
    private String address;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public static OrderUserInfo createOrderUserInfo(String username, String address, Order order){
        OrderUserInfo orderUserInfo = new OrderUserInfo();
        orderUserInfo.username = username;
        orderUserInfo.address = address;
        orderUserInfo.order = order;
        return orderUserInfo;
    }
}
