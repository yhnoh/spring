package com.example.persistencecontext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceContextSpecTest {

    /**
     * 영속성 컨텍스트에서 관리되고 있는 상태의 엔티티들을 조회시,
     * DB를 통해서 데이터를 조회하는 것이 아닌
     * 영속성 컨택스트에서 관리되고 있는 엔티티를 조회한다.
     * 이를 "1차캐시에서 조회한다"라고 한다.
     * 생각해보면 이미 영속성 컨텍스트 내에서 관리되고 있는 엔티티의 상태가 변경되었다고 해도
     * 엔티티 변경 -> DB 반영 순 이기 때문에 조회시 굳이 DB를 접근하여 조회할 필요성이 없다.
     *
     * 만약 1차 캐시에서 조회할 엔티티가 없다면 그때는 DB에 접근하여 데이터를 조회한 이후 영속성 컨텍스트에서 관리한다.
     */
    @Test
    public void cacheTest(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Member member = Member.createMember("id", "username");
            entityManager.persist(member);

            //select 문 실행 안하고 1차캐시에서 조회한다.
            Member findMember = entityManager.find(Member.class, "id");

            //영속성 컨텍스트에서 관리되고 있는 엔티티가 아니기 때문에
            //select 문 실행하고 DB에서 데이터를 조회한다.
            Member findMember2 = entityManager.find(Member.class, "id2");
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }

    /**
     * 동일성 보장
     * 엔티티를 영속성 컨텍스트에 관리함으로 인해서,
     * 해당 엔티티를 조회하면 1차 캐시 내에 있는 엔티티를 가지고 오기 때문에
     * 동일성을 보장한다.
     */
    @Test
    @DisplayName("동일성 보장")
    public void identificationTest(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            //비영속 상태
            Member member = Member.createMember("id", "username");
            //영속 상태 : 영속 컨텍스트에 관리
            entityManager.persist(member);
            entityManager.flush();

            //동일성 보장
            Member sameMember1 = entityManager.find(Member.class, "id");
            Member sameMember2 = entityManager.find(Member.class, "id");

            assertEquals(sameMember1, sameMember2);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    @Test
    @DisplayName("트랜잭션을 지원하는 쓰기 지연")
    public void lazyWriteTest(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Member member1 = Member.createMember("id1", "username1");
            Member member2 = Member.createMember("id2", "username2");

            //영속 컨텍스트에 관리된 상태이며 데이터베이스에 INSERT하지 않는다.
            entityManager.persist(member1);
            entityManager.persist(member2);

            System.out.println("======================쓰기 지연======================");

            //커밋
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    /**
     * ORM은 객체지향 관점에서 데이터베이스를 다루는 기술이다.
     * 객체의 필드 변경이 곧 SQL의 업데이트문 실행과 같다.
     * 그렇기 때문에 굳이 update문을 위한 메소드가 entityManager에 없다.
     * merge(entity)와는 조금 다르다.
     */
    @Test
    @DisplayName("변경 감지")
    public void dirtyCheckTest(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Member member = Member.createMember("id","username");
            entityManager.persist(member);
            entityManager.flush();
            entityManager.clear();

            //변경 감지
            Member findMember = entityManager.find(Member.class, "id");
            findMember.setUsername("username2");

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }
}
