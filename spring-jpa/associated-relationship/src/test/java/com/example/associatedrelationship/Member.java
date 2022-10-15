package com.example.associatedrelationship;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.*;

@Entity
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
}
