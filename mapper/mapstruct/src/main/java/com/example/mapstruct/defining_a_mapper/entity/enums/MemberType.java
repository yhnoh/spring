package com.example.mapstruct.defining_a_mapper.entity.enums;

public enum MemberType {

    MEMBER("일반회원"), ADMIN("관리자");

    private String name;

    MemberType(String name) {
        this.name = name;
    }
}
