package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.MemberDTO;
import com.example.mapstruct.entity.Member;
import com.example.mapstruct.repository.MemberJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(MemberMapperImpl.class)
public class MemberMapperTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private MemberMapper memberMapper;

    @BeforeEach
    public void setup() {
        Member member = Member.createMember("member1");
        memberJpaRepository.save(member);
    }

    @Test
    public void toDtoTest() {
        Member findMember = memberJpaRepository.findByUsername("member1");
        MemberDTO memberDTO = memberMapper.toDTO(findMember);

        Assertions.assertThat(memberDTO.getUsername()).isEqualTo("member1");
    }
}