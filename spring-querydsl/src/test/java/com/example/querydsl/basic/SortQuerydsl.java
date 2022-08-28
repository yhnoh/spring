package com.example.querydsl.basic;


import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberRepository;
import com.example.querydsl.repository.TeamRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.example.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class SortQuerydsl {
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;


    @BeforeEach
    public void setup(){

        memberRepository.deleteAll();
        teamRepository.deleteAll();

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.saveAll(Arrays.asList(teamA, teamB));

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        memberRepository.saveAll(Arrays.asList(member1, member2, member3, member4));

    }

    /**
     * orderBy를 이용해 정렬
     * Q클래스의 필드에서 desc, asc 정렬 가능
     * nullLast, nullFirst를 통해서 null 정렬 우선순위 부여 가능
     */
    @Test
    public void sort(){

        memberRepository.save(new Member(null, 100));

        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc().nullsFirst())
                .fetch();

        Assertions.assertNull(members.get(0).getUsername());
        Assertions.assertEquals(100, members.get(0).getAge());
        Assertions.assertEquals("member1", members.get(4).getUsername());
        Assertions.assertEquals(10, members.get(4).getAge());

    }

}
