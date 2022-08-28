package com.example.querydsl.basic;

import com.example.querydsl.entity.*;
import com.example.querydsl.entity.dto.AliasMemberDto;
import com.example.querydsl.entity.dto.MemberDto;
import com.example.querydsl.entity.dto.QMemberDto;
import com.example.querydsl.repository.MemberRepository;
import com.example.querydsl.repository.TeamRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
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
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProjectionQuerydsl {
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
     * 프로젝션 대상이 하나이면 타입을 명확하게 지정 가능
     */
    @Test
    @DisplayName("조회 - 대상이 하나일 경우")
    public void one_projection(){

        List<String> usernames = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        assertThat(usernames).containsExactly("member1", "member2", "member3", "member4");
    }

    /**
     * 프로젝션 대상이 두개 이상이면 튜플이나 DTO 조회
     */
    @Test
    @DisplayName("조회 - 대상이 여러개일 경우 튜플로 반환")
    public void many_projection(){

        List<Tuple> tuples = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : tuples) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println("username = " + username + ", age = " + age);
        }
    }

    /**
     * JPQL을 이용한 DTO 조회시 불편하점
     * 1. DTO로 변환하기 위해서 package 이름을 다 적어요줘여한다.
     * 2. 생성자가 있어야 변환이 가능하다.
     */
    @Test
    @DisplayName("결과를 DTO로 반환 - JPQL을 이용")
    public void jpql_mapping_entity_to_dto(){
        List<MemberDto> members = em.createQuery("select new com.example.querydsl.entity.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();

        Assertions.assertThat(members)
                .extracting("username", "age")
                .contains(
                        org.assertj.core.groups.Tuple.tuple("member1", 10),
                        org.assertj.core.groups.Tuple.tuple("member2", 20),
                        org.assertj.core.groups.Tuple.tuple("member3", 30),
                        org.assertj.core.groups.Tuple.tuple("member4", 40)
                        );
    }

    /**
     * setter 조회
     * 1. 기본 생성자 필요
     * 2. setter 메서드를 이용할때는 dto 필드와 entity 필드가 일치해야한다.
     */
    @Test
    @DisplayName("결과를 DTO로 반환 - querydsl이 setter 메소드에 접근하여 dto 반환")
    public void querydsl_mapping_setter_entity_to_dto(){

        List<MemberDto> members = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        Assertions.assertThat(members)
                .extracting("username", "age")
                .contains(
                        org.assertj.core.groups.Tuple.tuple("member1", 10),
                        org.assertj.core.groups.Tuple.tuple("member2", 20),
                        org.assertj.core.groups.Tuple.tuple("member3", 30),
                        org.assertj.core.groups.Tuple.tuple("member4", 40)
                );


    }

    /**
     * dto 필드와 entity 필드가 일치해야한다.
     */
    @Test
    @DisplayName("결과를 DTO로 반환 - querydsl이 필드에 접근하여 dto 반환")
    public void querydsl_mapping_field_entity_to_dto(){

        List<MemberDto> members = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        Assertions.assertThat(members)
                .extracting("username", "age")
                .contains(
                        org.assertj.core.groups.Tuple.tuple("member1", 10),
                        org.assertj.core.groups.Tuple.tuple("member2", 20),
                        org.assertj.core.groups.Tuple.tuple("member3", 30),
                        org.assertj.core.groups.Tuple.tuple("member4", 40)
                );

    }

    /**
     * setter 메소드, 필드에 접근하여 변환할때 엔티티 필드와 일치하지 않느경우
     * 별칭을 활용할 수 있다.
     *
     * member.username.as(alias)
     * com.querydsl.core.types.ExpressionUtils.as(sources, alias)
     */
    @Test
    @DisplayName("결과를 DTO로 반환 - entity와 dto의 멤버변수가 다를 때 별칭을 이용해 dto 반환")
    public void querydsl_mapping_alias_entity_to_dto(){
        QMember memberSub = new QMember("memberSub");
        List<AliasMemberDto> members = queryFactory
                .select(Projections.fields(AliasMemberDto.class,
                        member.username.as("name"),
                        ExpressionUtils.as(
                                JPAExpressions.select(memberSub.age.max())
                                        .from(memberSub)
                                ,"age"
                        )
                ))
                .from(member)
                .fetch();

        Assertions.assertThat(members)
                .extracting("name", "age")
                .contains(
                        org.assertj.core.groups.Tuple.tuple("member1", 40),
                        org.assertj.core.groups.Tuple.tuple("member2", 40),
                        org.assertj.core.groups.Tuple.tuple("member3", 40),
                        org.assertj.core.groups.Tuple.tuple("member4", 40)
                );

    }


    /**
     * 생성자를 사용해 entity를 DTO로 반환할 수 있다.
     * 생성자 위치와 타입이 동일해야한다.
     */
    @Test
    @DisplayName("결과를 DTO로 반환 - querydsl이 생성자에 접근하여 dto 반환")
    public void querydsl_mapping_constructor_entity_to_dto(){

        List<MemberDto> members = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        Assertions.assertThat(members)
                .extracting("username", "age")
                .contains(
                        org.assertj.core.groups.Tuple.tuple("member1", 10),
                        org.assertj.core.groups.Tuple.tuple("member2", 20),
                        org.assertj.core.groups.Tuple.tuple("member3", 30),
                        org.assertj.core.groups.Tuple.tuple("member4", 40)
                );

    }

    /**
     * @QueryProjection 활용
     * 컴파일로 타입을 체크할 수 있는 가장 안전한 방법이지만,
     * 어노테이션을 DTO에 유지해야한다는 점과 Q파일을 생성해야한다는 단점이 있다.
     */
    @Test
    @DisplayName("결과를 DTO로 반환 - @QueryProjection 활용")
    public void querydsl_mapping_queryprojection_entity_to_dto(){

        List<MemberDto> members = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        Assertions.assertThat(members)
                .extracting("username", "age")
                .contains(
                        org.assertj.core.groups.Tuple.tuple("member1", 10),
                        org.assertj.core.groups.Tuple.tuple("member2", 20),
                        org.assertj.core.groups.Tuple.tuple("member3", 30),
                        org.assertj.core.groups.Tuple.tuple("member4", 40)
                );

    }


}
