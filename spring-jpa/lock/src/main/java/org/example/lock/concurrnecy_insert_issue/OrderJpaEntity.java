package org.example.lock.concurrnecy_insert_issue;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
@Entity
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "order_num", unique = true)
    private String orderNum;

    public OrderJpaEntity(String orderNum) {
        this.orderNum = orderNum;
    }
}
