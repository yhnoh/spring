package com.example.querydsl.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchCondition {
    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;
}
