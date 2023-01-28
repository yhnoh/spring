package com.example.mapstruct.entity;

import com.example.mapstruct.entity.enums.MemberStatus;
import com.example.mapstruct.entity.enums.MemberType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Builder와 @NoArgsConstructor를 같이 사용하면 @Builder는 사용할 매개변수가 없다.
 * Entity를 사용하게 되면 적어도 protected 이상의 기본 생성자가 필요하다.
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private LocalDateTime createdDatetime;

    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;

    public static Member createMember(String username) {
        Member member = new Member();
        member.username = username;
        member.createdDatetime = LocalDateTime.now();
        member.memberType = MemberType.MEMBER;
        member.memberStatus = MemberStatus.ACTIVE;
        return member;
    }
}

