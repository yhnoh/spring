package com.example.querydsl.entity;

import com.example.querydsl.repository.MemberJpaRepository;
import com.example.querydsl.repository.TeamJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StartQuerydsl {

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

    @Test
    public void startJPQL(){
        String jpql = "select m from Member m where m.username = :username";

        Member findMember = em.createQuery(jpql, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertEquals("member1", findMember.getUsername());

    }

    @Test
    public void startQuerydsl(){
        QMember m = QMember.member;
        Member findMember = queryFactory.select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertEquals("member1", findMember.getUsername());

    }
}
