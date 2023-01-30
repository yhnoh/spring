package com.example.mapstruct.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class OrderDTO {

    private Long id;
    private String orderName;
    private LocalDateTime createdDatetime;
    private Integer quantity;
    private MemberDTO member;
}
