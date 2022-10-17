package com.example.associatedrelationship.relationship;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public static Member createMember(String username, Team team){
        Member member = new Member();
        member.username = username;
        member.team = team;
        team.addMember(member);
        return member;
    }

    public static Member createMemberNotAddMemberInTeam(String username, Team team){
        Member member = new Member();
        member.username = username;
        member.team = team;
        //team.addMember(member); 호출 안함
        return member;

    }
}
