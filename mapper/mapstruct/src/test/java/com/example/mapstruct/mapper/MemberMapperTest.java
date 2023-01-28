package com.example.mapstruct.mapper;

import com.example.mapstruct.dto.MemberDTO;
import com.example.mapstruct.entity.Member;
import com.example.mapstruct.repository.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MemberMapperImpl.class)
public class MemberMapperTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private MemberMapper memberMapper;

    /**
     * 기본적으로 기본생성자와 빌더를 통해서 컴파일 시점에 @Mapper 어노테이션을 기반으로 코드를 생성한다.
     * intellij mapstruct plugin : https://plugins.jetbrains.com/plugin/10036-mapstruct-support
     */
    @Test
    @DisplayName("entity 에서 dto")
    public void toDtoTest() {

        Member member = Member.createMember("member1");
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findByUsername("member1");
        MemberDTO memberDTO = memberMapper.toMemberDTO(findMember);

        assertThat(memberDTO.getUsername()).isEqualTo("member1");
    }

    @Test
    public void toEntityTest() {
        MemberDTO memberDTO = MemberDTO.builder()
                .id(1L)
                .username("member1")
                .registerDate(LocalDateTime.of(2023, 01, 28, 00, 00))
                .build();

        Member member = memberMapper.toMember(memberDTO);

        assertThat(member.getId()).isNull();
        assertThat(member.getUsername()).isEqualTo("member1");
        assertThat(member.getRegisterDate()).isEqualTo(LocalDateTime.of(2023, 01, 28, 00, 00));

        Member saveMember = memberJpaRepository.save(member);

        assertThat(saveMember.getId()).isNotNull();
        assertThat(saveMember.getUsername()).isEqualTo("member1");
        assertThat(saveMember.getRegisterDate()).isEqualTo(LocalDateTime.of(2023, 01, 28, 00, 00));

    }
}