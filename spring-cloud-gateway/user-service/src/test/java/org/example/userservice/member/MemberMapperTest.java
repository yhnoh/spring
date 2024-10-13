package org.example.userservice.member;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MemberMapperTest {


    @InjectMocks
    private MemberMapperImpl mapper;

    @Test
    public void toMemberTest() {
        //given
        MemberJpaEntity entity = MemberJpaEntity.builder()
                .id(1L)
                .loginId("loginId")
                .loginPassword("password")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        //when
        Member result = mapper.toMember(entity);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getLoginId()).isEqualTo(entity.getLoginId());
        assertThat(result.getLoginPassword()).isEqualTo(entity.getLoginPassword());
        assertThat(result.getRole()).isEqualTo(entity.getRole());
        assertThat(result.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(result.getModifiedAt()).isEqualTo(entity.getModifiedAt());
    }
}