package com.example.querydsl.basic;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberRepository;
import com.example.querydsl.repository.TeamRepository;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.example.querydsl.entity.QMember.member;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ResultQuerydsl {


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

    @Test
    @DisplayName("결과 조회")
    public void result(){
        /**
         * fetch은 리스트 조회, 데이터가 없으면 빈 리스트 반환
         */
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        assertEquals(4, fetch.size());

        /**
         * fetchOne은 단건 조회
         * 결과가 없으면 null을 반환
         * 결과가 둘 이상이면 com.querydsl.core.NonUniqueResultException 에러 발생
         */
        Member fetchOne = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        assertEquals("member1", fetchOne.getUsername());

        Member fetchOneNull = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member5"))
                .fetchOne();

        assertNull(fetchOneNull);

        assertThrows(NonUniqueResultException.class, () -> {
            Member fetchOneOverOne = queryFactory
                    .selectFrom(member)
                    .fetchOne();
        });

        /**
         * fetchFirst : 가장 최상단의 하나만 조회
         */
        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();
        assertEquals("member1", fetchFirst.getUsername());

    }

}
