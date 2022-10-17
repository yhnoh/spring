package com.example.associatedrelationship.relationship;

import com.example.associatedrelationship.NotFoundDataException;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();


    public static Team createTeam(String teamName){
        Team team = new Team();
        team.teamName = teamName;
        return team;
    }

    public void addMember(Member member){
        if(member == null){
            throw new NotFoundDataException("member is null when add member in team");
        }
        members.add(member);
    }

}
