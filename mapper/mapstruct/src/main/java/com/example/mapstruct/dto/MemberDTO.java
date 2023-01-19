package com.example.mapstruct.dto;

import com.example.mapstruct.entity.Member;
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
    private LocalDateTime registerDate;

    public Member toEntity() {
        return Member.createMember(username);
    }
}
