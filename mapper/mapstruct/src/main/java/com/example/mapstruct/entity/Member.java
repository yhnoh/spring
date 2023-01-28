package com.example.mapstruct.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private LocalDateTime registerDate;


    public static Member createMember(String username) {
        Member member = new Member();
        member.username = username;
        member.registerDate = LocalDateTime.now();
        return member;
    }
}

