package org.example.userservice.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MemberResponseMapperTest {

    @InjectMocks
    private MemberResponseMapperImpl mapper;

    @Test
    public void toMemberTest() {
        //given
        Member member = Member.builder()
                .id(1L)
                .loginId("loginId")
                .loginPassword("password")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        //when
        MemberResponse result = mapper.toMemberResponse(member);
        
        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(result.getLoginPassword()).isEqualTo(member.getLoginPassword());
        assertThat(result.getRole()).isEqualTo(member.getRole());
        assertThat(result.getCreatedAt()).isEqualTo(member.getCreatedAt());
        assertThat(result.getModifiedAt()).isEqualTo(member.getModifiedAt());
    }
}