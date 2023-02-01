package com.example.mapstruct.defining_a_mapper._03_adding_custom_methods_to_mappers;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import com.example.mapstruct.defining_a_mapper.entity.Order;
import com.example.mapstruct.defining_a_mapper.repository.MemberJpaRepository;
import com.example.mapstruct.defining_a_mapper.repository.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CustomMethodMapperImpl.class)
@DataJpaTest
public class CustomMethodMapperTest {

    @Autowired
    private CustomMethodMapper customMethodMapper;

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    private Member member;
    private Order order;

    @BeforeEach
    public void setup() {
        member = Member.createMember("username");
        memberJpaRepository.save(member);

        order = Order.createOrder("orderName", 1, member);
        orderJpaRepository.save(order);
    }

    @Test
    public void toAutoMemberDTOTest() {
        MemberDTO memberDTO = customMethodMapper.toAutoMemberDTO(member);

        assertThat(memberDTO.getOrders()).extracting("id").first().isNotNull();
    }

    @Test
    public void toMemberOrdersDTOTest() {
        MemberDTO memberDTO = customMethodMapper.toAutoMemberDTO(member);

        assertThat(memberDTO.getOrders()).extracting("id").first().isNotNull();
    }

    @Test
    public void toQualifiedMemberDTOTest() {
        MemberDTO memberDTO = customMethodMapper.toQualifiedMemberDTO(member);

        assertThat(memberDTO.getOrders()).extracting("id").first().isNull();
    }


}