package com.example.springbatchdatabase.repository;

import com.example.springbatchdatabase.entity.Member;
import com.example.springbatchdatabase.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class MemberJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void defaultBatchFetchSizeTest() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Member member = Member.builder()
                    .username("username")
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

        entityManager.flush();
        entityManager.clear();

        List<Member> findMembers = memberJpaRepository.findAll();

        findMembers.get(0).getOrders().get(0);
        findMembers.get(1).getOrders().get(1);
    }

}
