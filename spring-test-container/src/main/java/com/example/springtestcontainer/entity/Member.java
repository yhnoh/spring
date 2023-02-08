package com.example.springtestcontainer.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createdDateTime;

    public static Member createMember(String username, String password){
        Member member = new Member();
        member.username = username;
        member.password = password;
        member.createdDateTime = LocalDateTime.now();
        return member;
    }

    public void changeUsername(String username){
        this.username = username;
    }
}
