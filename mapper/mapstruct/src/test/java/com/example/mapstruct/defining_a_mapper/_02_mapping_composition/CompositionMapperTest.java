package com.example.mapstruct.defining_a_mapper._02_mapping_composition;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import com.example.mapstruct.defining_a_mapper.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@Import(CompositionMapperImpl.class)
@DataJpaTest
public class CompositionMapperTest {

    @Autowired
    private CompositionMapper compositionMapper;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    private Member member;


    @BeforeEach
    public void setup() {
        member = Member.createMember("username");
        memberJpaRepository.save(member);
    }

    @Test
    public void toMemberDTOTest() {
        MemberDTO memberDTO = compositionMapper.toMemberDTO(member);

        assertThat(memberDTO.getId()).isNull();
        assertThat(memberDTO.getMemberName()).isEqualTo("username");
    }
}