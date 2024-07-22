package org.example.springbatchitemwriterjpa.jpa.member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Builder.Default
    private long orderCount = 0;

    public void changeOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }
}
