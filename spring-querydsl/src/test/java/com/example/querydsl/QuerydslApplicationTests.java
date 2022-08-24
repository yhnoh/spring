package com.example.querydsl;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void startQuerydsl() {
        //insert
        Member member = new Member("member1");
        entityManager.persist(member);

        //Querydsl의 Q 타입 사용
        QMember qMember = QMember.member;

        //JPAQueryFactory를 가져와서 질의
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        Member findMember = query.selectFrom(qMember)
                .fetchOne();

        Assertions.assertEquals(1, findMember.getId());
        Assertions.assertEquals("member1", findMember.getUsername());

    }

}
