package com.example.mapstruct.defining_a_mapper._04_mapping_methods_with_several_source_parameters;


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

@Import(SeveralSourceParametersMapperImpl.class)
@DataJpaTest
public class SeveralSourceParametersMapperTest {

    @Autowired
    private SeveralSourceParametersMapper severalSourceParametersMapper;

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
    public void toMemberOrderDTOTest() {
        MemberOrderDTO memberOrderDTO = severalSourceParametersMapper.toMemberOrderDTO(member, order);
        
        assertThat(memberOrderDTO.getUsername()).isEqualTo("username");
        assertThat(memberOrderDTO.getOrderName()).isEqualTo("orderName");
    }

}