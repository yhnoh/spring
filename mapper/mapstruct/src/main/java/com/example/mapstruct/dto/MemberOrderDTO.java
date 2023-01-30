package com.example.mapstruct.dto;

import com.example.mapstruct.entity.enums.MemberStatus;

public class MemberOrderDTO {

    private Long id;

    private String username;

    private String memberType;
    private MemberStatus memberStatus;

    private String orderName;
    private int quantity;
}
