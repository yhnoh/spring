package com.example.querydsl.basic;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberRepository;
import com.example.querydsl.repository.TeamRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static com.example.querydsl.entity.QMember.*;
import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SubQuerydsl {
    @Autowired
    JPAQueryFactory queryFactory;

    @PersistenceContext
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
    @DisplayName("서브쿼리 - where 절에 == 사용")
    public void subQuery(){
        QMember memberSub = new QMember("memberSub");

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getUsername()).isEqualTo("member4");
        assertThat(members.get(0).getAge()).isEqualTo(40);

    }

    @Test
    @DisplayName("서브쿼리 - where절에 >= 사용")
    public void goe_sub_query(){
        QMember memberSub = new QMember("memberSub");

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members).extracting("age").containsExactly(30, 40);

    }

    @Test
    @DisplayName("서브쿼리 - where절에 in절 사용")
    public void in_sub_query(){
        QMember memberSub = new QMember("memberSub");

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(members.size()).isEqualTo(3);
        assertThat(members).extracting("age").containsExactly(20, 30, 40);

    }

    @Test
    @DisplayName("서브쿼리 - select절 사용")
    public void select_sub_query(){
        QMember memberSub = new QMember("memberSub");

        List<Tuple> tuples = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(member)
                .fetch();
        for (Tuple tuple : tuples) {
            System.out.println("username = " + tuple.get(member.username) + "avg age = " + tuple.get(memberSub.age.avg()));
        }
    }

    /**
     * JPA, Querydsl은 from 절에 서브쿼리를 지원하지 않는다.
     *
     * from 절의 서브쿼리 해결방안
     * 서브쿼리를 join으로 변경한다. (가능한상황도 있고 불가능한 상황도 있다.)
     * 애플리케이션에서 쿼리를 2번 실행한다.
     * nativeSQL을 활용한다.
     */
}
