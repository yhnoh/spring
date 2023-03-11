package com.example.springbatchdatabase;

import com.example.springbatchdatabase.entity.Member;
import com.example.springbatchdatabase.entity.Order;
import com.example.springbatchdatabase.repository.MemberJpaRepository;
import com.example.springbatchdatabase.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

//@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitialization {

    private final MemberJpaRepository memberJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    @PostConstruct
    @Transactional
    public void postConstruct() {

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = Member.builder()
                    .username("username" + i)
                    .build();
            members.add(member);
        }

        List<Member> saveMembers = memberJpaRepository.saveAll(members);

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < saveMembers.size(); i++) {
            Member saveMember = saveMembers.get(i);
            for (int j = 0; j < 2; j++) {
                Order order = Order.builder()
                        .orderName(saveMember.getUsername() + " ordered a goods " + i)
                        .member(saveMember)
                        .build();
                orders.add(order);
            }
        }

        orderJpaRepository.saveAll(orders);
    }

}
