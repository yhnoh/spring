package com.example.querydsl.repository;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.Team;
import com.example.querydsl.entity.dto.MemberSearchCondition;
import com.example.querydsl.entity.dto.MemberTeamDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

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
    @DisplayName("JPA를 이용한 repository 테스트")
    public void jpa_repository(){

        List<Member> findMembers = memberJpaRepository.findAll();
        assertEquals(4, findMembers.size());

        List<Member> findMemberByUsername = memberJpaRepository.findByUsername("member1");
        assertEquals(1, findMemberByUsername.size());


    }

    @Test
    @DisplayName("Querydsl을 이용한 repositoy 테스트")
    public void querydsl_repository(){

        List<Member> findMembers = memberJpaRepository.findAll_Querydsl();
        assertEquals(4, findMembers.size());

        List<Member> findMembersByUsername = memberJpaRepository.findByUsername_Querydsl("member1");
        assertEquals(1, findMembersByUsername.size());

    }

    @Test
    @DisplayName("querydsl - booleanBuilder를 이용한 검색 테스트")
    public void querydsl_search_booleanBuilder(){
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberRepository.searchByBuilder(condition);

        Assertions
                .assertThat(result)
                .extracting("username")
                .contains("member4");

    }

    @Test
    @DisplayName("querydsl - where절 다중 파라미터를 이용한 검색 테스트")
    public void querydsl_search_parameters(){
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

        List<MemberTeamDto> result = memberRepository.searchByParameters(condition);

        Assertions
                .assertThat(result)
                .extracting("username")
                .contains("member4");
    }

    @Test
    public void querydsl_search_page(){

        MemberSearchCondition condition = new MemberSearchCondition();
        PageRequest of = PageRequest.of(0, 1);
        Page<MemberTeamDto> paging = memberRepository.searchPaging(condition, of);

        assertEquals(1, paging.getContent().size());
        assertEquals(4, paging.getTotalPages());

    }

}