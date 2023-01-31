package com.example.mapstruct.defining_a_mapper._01_basic_mappings;

import com.example.mapstruct.defining_a_mapper.entity.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class MemberDTO {

    private Long id;

    private String memberName;

    private LocalDateTime createdDatetime;
    private String memberType;
    private MemberStatus memberStatus;

}
