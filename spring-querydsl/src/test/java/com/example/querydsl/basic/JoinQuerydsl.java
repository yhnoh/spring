package com.example.querydsl.basic;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.QTeam;
import com.example.querydsl.entity.Team;
import com.example.querydsl.repository.MemberJpaRepository;
import com.example.querydsl.repository.TeamJpaRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.Arrays;
import java.util.List;

import static com.example.querydsl.entity.QMember.*;
import static com.example.querydsl.entity.QTeam.*;
import static com.example.querydsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JoinQuerydsl {
    @Autowired
    JPAQueryFactory queryFactory;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @PersistenceUnit
    EntityManagerFactory emf;


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
    @DisplayName("기본 join절 사용하기")
    public void join(){
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        List<Member> members = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(members.get(0).getTeam());
        assertThat(loaded).isTrue();
        assertThat(members).extracting("username").containsExactly("member1", "member2");
    }

    @Test
    @DisplayName("세타조인 사용하기 - 연관관계가 업는 필드로 조인")
    public void thetaJoin(){

        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> members = queryFactory.select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(members).extracting("username").containsExactly("teamA", "teamB");

    }

    @Test
    @DisplayName("외부 조인 on절 사용하여 대상 필터링")
    public void outer_join_on(){

        List<Tuple> tuples = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        assertThat(tuples.size()).isEqualTo(4);

        for (Tuple tuple : tuples) {
            Member member = tuple.get(QMember.member);
            Team team = tuple.get(QTeam.team);
            System.out.println("member = " + member +  ", team = " + team);
        }
    }

    /**
     * inner join으로 사용할 경우에는 거의 where 절과 동일한 기능으로 사용된다.
     * 그러므로 inner join을 사용할때는 일반 where절을 해결하는것이 더 코드를 이해하기 쉽다.
     */
    @Test
    @DisplayName("내부 조인 on절 사용하여 대상 필터링")
    public void inner_join_on(){

        List<Tuple> tuples = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        assertThat(tuples.size()).isEqualTo(2);

        for (Tuple tuple : tuples) {
            Member member = tuple.get(QMember.member);
            Team team = tuple.get(QTeam.team);
            System.out.println("member = " + member +  ", team = " + team);
        }

    }

    @Test
    @DisplayName("페치 조인 사용안함")
    public void no_fetch_join(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).isFalse();
    }

    /**
     * SQL 조인을 활용하여 연관된 엔티티를 SQL 한번에 조회하는 기능
     */
    @Test
    @DisplayName("페치 조인 사용안함")
    public void fetch_join(){
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .join(member.team, team).fetchJoin()
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).isTrue();

    }

}
