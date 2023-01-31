package com.example.mapstruct.defining_a_mapper._03_adding_custom_methods_to_mappers;

import com.example.mapstruct.defining_a_mapper.entity.enums.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
public class MemberDTO {

    private Long id;

    private String username;

    private LocalDateTime createdDatetime;
    private String memberType;
    private MemberStatus memberStatus;

    private List<OrderDTO> orders;
}
