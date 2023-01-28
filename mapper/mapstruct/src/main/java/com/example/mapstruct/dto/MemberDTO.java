package com.example.mapstruct.dto;

import com.example.mapstruct.entity.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class MemberDTO {

    private Long id;

    private String username;

    private LocalDateTime createdDatetime;
    private String memberType;
    private MemberStatus memberStatus;

}
