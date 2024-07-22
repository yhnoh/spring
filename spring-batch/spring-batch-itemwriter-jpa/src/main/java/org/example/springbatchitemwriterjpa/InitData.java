package org.example.springbatchitemwriterjpa;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData {

    private final MemberService memberService;
    private final OrderService orderService;


    @PostConstruct
    public void init() {
        MemberJpaEntity member1 = memberService.save("member1");
        OrderJpaEntity order1 = orderService.save(member1.getId());

        System.out.println("member1 = " + member1);
        System.out.println("order1 = " + order1);
    }
}
