package com.example.mapstruct.defining_a_mapper._01_basic_mappings;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import com.example.mapstruct.defining_a_mapper.entity.enums.MemberStatus;
import com.example.mapstruct.defining_a_mapper.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(BasicMapperImpl.class)
@DataJpaTest
public class BasicMapperTest {

    @Autowired
    private BasicMapper basicMapper;
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
        MemberDTO memberDTO = basicMapper.toMemberDTO(member);

        assertThat(memberDTO.getId()).isNotNull();
        assertThat(memberDTO.getMemberName()).isEqualTo("username");
        assertThat(memberDTO.getCreatedDatetime()).isNotNull();
        assertThat(memberDTO.getMemberType()).isEqualTo("MEMBER");
        assertThat(memberDTO.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);

    }
}