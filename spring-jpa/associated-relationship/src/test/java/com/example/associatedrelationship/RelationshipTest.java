package com.example.associatedrelationship;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RelationshipTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    @Transactional
    public void saveTest(){
        Team team = Team.createTeam("한국");
        teamJpaRepository.save(team);

        Member member = Member.createMember("member1", team);
        memberJpaRepository.save(member);
        
    }

}
