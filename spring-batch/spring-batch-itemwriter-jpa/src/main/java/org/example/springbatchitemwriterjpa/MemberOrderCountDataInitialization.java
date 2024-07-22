package org.example.springbatchitemwriterjpa;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberOrderCountDataInitialization {

    private final MemberService memberService;
    private final OrderService orderService;

    @PostConstruct
    public void init() {
        MemberJpaEntity member1 = memberService.save("member1");
        for (int i = 0; i < 10; i++) {
            OrderJpaEntity order = orderService.save(member1.getId());
        }

        MemberJpaEntity member2 = memberService.save("member2");
        for (int i = 0; i < 5; i++) {
            OrderJpaEntity order = orderService.save(member2.getId());
        }

    }
}
