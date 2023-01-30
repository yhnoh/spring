package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.MemberDTO;
import com.example.mapstruct.dto.OrderDTO;
import com.example.mapstruct.entity.Member;
import com.example.mapstruct.entity.Order;
import com.example.mapstruct.repository.MemberJpaRepository;
import com.example.mapstruct.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(OrderMapperImpl.class)
public class OrderMapperTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void toOrderDTOTest() {
        Member member = Member.createMember("member");
        Member saveMember = memberJpaRepository.save(member);

        Order order = Order.createOrder("orderName", 1, saveMember);
        Order saveOrder = orderJpaRepository.save(order);

        OrderDTO orderDTO = orderMapper.toOrderDTO(saveOrder);

        assertThat(orderDTO.getId()).isNotNull();
        assertThat(orderDTO.getOrderName()).isEqualTo("orderName");
        assertThat(orderDTO.getCreatedDatetime()).isNotNull();
        assertThat(orderDTO.getQuantity()).isEqualTo(1);

        MemberDTO memberDTO = orderDTO.getMember();
        assertThat(memberDTO.getUsername()).isEqualTo("member");

    }

}
