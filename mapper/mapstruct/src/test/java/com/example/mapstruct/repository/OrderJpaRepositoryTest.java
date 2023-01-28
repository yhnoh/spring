package com.example.mapstruct.repository;

import com.example.mapstruct.entity.Member;
import com.example.mapstruct.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest

public class OrderJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    @DisplayName("주문 생성 테스트")
    public void saveOrderTest() {
        Member member = Member.createMember("member");
        Member saveMember = memberJpaRepository.save(member);

        Order order = Order.createOrder("orderName", 1, saveMember);
        Order saveOrder = orderJpaRepository.save(order);

        assertThat(saveOrder.getMember()).isEqualTo(saveMember);
        assertThat(saveOrder.getOrderName()).isEqualTo("orderName");
        assertThat(saveOrder.getQuantity()).isEqualTo(1);
    }
}
