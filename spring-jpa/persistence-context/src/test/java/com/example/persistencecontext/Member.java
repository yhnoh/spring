package com.example.persistencecontext;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    private String id;
    private String username;

    public static Member createMember(String id, String username){
        Member member = new Member();
        member.id = id;
        member.username = username;
        return member;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
