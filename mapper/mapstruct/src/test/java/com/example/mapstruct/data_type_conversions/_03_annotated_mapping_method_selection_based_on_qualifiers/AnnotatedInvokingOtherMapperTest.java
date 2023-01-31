package com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AnnotatedInvokingOtherMapperImpl.class)
public class AnnotatedInvokingOtherMapperTest {

    @Autowired
    private AnnotatedInvokingOtherMapper annotatedInvokingOtherMapper;

    @Test
    public void toMemberDTOTest() {
        Member member = Member.builder()
                .build();
        MemberDTO memberDTO = annotatedInvokingOtherMapper.toMemberDTO(member);

        assertThat(memberDTO.getUsername()).isEqualTo("username2");
        assertThat(memberDTO.getCreatedDateTime()).isEqualTo("2023-01-31T00:00:00");
        assertThat(memberDTO.getModifiedDateTime()).isEqualTo(LocalDateTime.of(2023, 01, 31, 0, 0));

    }


}