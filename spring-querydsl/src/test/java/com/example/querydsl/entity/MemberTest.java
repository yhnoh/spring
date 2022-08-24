package com.example.querydsl.entity;

import com.example.querydsl.repository.MemberJpaRepository;
import com.example.querydsl.repository.TeamJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;


    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamJpaRepository.saveAll(Arrays.asList(teamA, teamB));

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        memberJpaRepository.saveAll(Arrays.asList(member1, member2, member3, member4));

        List<Member> members = memberJpaRepository.findAll();

        for (Member member : members) {
            System.out.println("member = " + member + ", " + "member.getTeam() = " + member.getTeam());
        }
    }
}
