package com.example.querydsl.basic;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberJpaRepository;
import com.example.querydsl.repository.TeamJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class DynamicQuerydsl {

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
    @DisplayName("동적 쿼리 - BooleanBuilder 활용")
    public void booleanBuilder(){
        String usernameParam = "member1";
        Integer ageParam = 10;

        BooleanBuilder builder = new BooleanBuilder();
        if(usernameParam != null){
            builder.and(member.username.eq(usernameParam));
        }

        if(ageParam != null){
            builder.and(member.age.eq(ageParam));
        }

        List<Member> findMembers = queryFactory.selectFrom(member)
                .where(builder)
                .fetch();

        Assertions.assertEquals(1, findMembers.size());
    }

    /**
     * where 조건에 null 값은 무시되는 것을 활용한다.
     * BooleanExpression마다 메소드를 추출할 수 있다.
     * 메소드를 추출하여 네이밍으로 쿼리 가독성이 높일 수 있으며, 재활용이 가능해진다.
     * BooleanExpression 마다 and, or 메서드 체이닝을 걸어 조합도 가능하다.
     */
    @Test
    @DisplayName("동적쿼리 - where절 다중 파라미터 활용")
    public void parameters(){

        String usernameParam = "member1";
        Integer ageParam = 10;

        BooleanExpression usernameEq = usernameParam != null ? member.username.eq(usernameParam) : null;
        BooleanExpression ageEq = ageParam != null ? member.age.eq(ageParam) : null;

        List<Member> findMembers = queryFactory.selectFrom(member)
                .where(usernameEq, ageEq)
                .fetch();

        Assertions.assertEquals(1, findMembers.size());

    }
}
