package com.example.querydsl.basic;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberRepository;
import com.example.querydsl.repository.TeamRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
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

@SpringBootTest
@Transactional
public class CaseQuerydsl {
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

    /**
     * case문은 select, where, order by에 사용이 가능하다.
     */

    @Test
    @DisplayName("case문")
    public void case_query(){
        List<String> list = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        Assertions.assertThat(list)
                .containsExactly("열살", "스무살", "기타", "기타");
    }

    @Test
    @DisplayName("case문 - CaseBuilder 사용")
    public void complex_case_query(){

        List<String> list = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        Assertions.assertThat(list)
                .containsExactly("0~20살", "0~20살", "21~30살", "기타");
    }

    @Test
    @DisplayName("case문 - case문을 활용하여 순서 정하기")
    public void order_case_query(){
        NumberExpression<Integer> ageRank = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> tuples = queryFactory
                .select(member.username, member.age, ageRank)
                .from(member)
                .orderBy(ageRank.desc())
                .fetch();

        for (Tuple tuple : tuples) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            Integer rank = tuple.get(ageRank);
            System.out.println("username = " + username + ", age = " + age + ", rank = " + rank);
        }
    }


}
