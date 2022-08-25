package com.example.querydsl.basic;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberJpaRepository;
import com.example.querydsl.repository.TeamJpaRepository;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.example.querydsl.entity.QMember.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SearchQuerydsl {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;


    @BeforeEach
    public void setup(){

        memberJpaRepository.deleteAll();
        teamJpaRepository.deleteAll();

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamJpaRepository.saveAll(Arrays.asList(teamA, teamB));

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        memberJpaRepository.saveAll(Arrays.asList(member1, member2, member3, member4));

    }

    /**
     * 검색 조건은 and, or 를 메서드 체인으로 연결할 수 있다.
     * JPQL이 제공하는 모든 검색조건을 제공해준다.
     */
    @Test
    @DisplayName("and, or 검색조건")
    public void search1(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertEquals("member1", findMember.getUsername());
    }

    /**
     * where에 파라미터로 검색조건을 추가할 수 있다.
     */
    @Test
    @DisplayName("AND 조건을 파라미터러 처리")
    public void search2(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),member.age.eq(10))
                .fetchOne();

        assertEquals("member1", findMember.getUsername());
    }


}
