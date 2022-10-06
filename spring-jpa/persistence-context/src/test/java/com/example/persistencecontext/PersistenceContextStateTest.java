package com.example.persistencecontext;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersistenceContextStateTest {


    /**
     * 영속성 컨텍스트와 전혀 관계가 없는 상태
     */
    @Test
    @DisplayName("비영속")
    public void transientTest() {
        assertEquals("username", Member.createMember("username").getUsername());
    }

    @Test
    @DisplayName("영속")
    public void managedTest() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();


        try {
            //비영속 상태
            Member member = Member.createMember("username");
            //영속 상태 : 영속 컨텍스트에 관리
            entityManager.persist(member);
            entityManager.flush();

            //영속 상태에서의 변경 감지 작동 확인
            member.setUsername("username2");
            assertTrue(((Session) entityManager).isDirty());
            entityManager.flush();

            //동일성 보장 확인
            Member findMember = entityManager.find(Member.class, member.getId());
            assertEquals(member, findMember);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }


    }

    /**
     * id (식별자) 값을 가지고 있지만 영속성 컨텍스트에서 더이상 관리 하지 않는 상태
     * 영속 상태에서 사용할 수 있는 기능을 활용할 수 없다.
     * <p>
     * entityManager.detach()
     * entityManager.close();
     * entityManager.clear();
     */
    @Test
    @DisplayName("비영속")
    public void detachTest() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            //비영속 상태
            Member member = Member.createMember("username");

            //영속 상태 : 영속 컨텍스트에 관리
            entityManager.persist(member);
            entityManager.flush();

            //준영속
            entityManager.detach(member);

            //변경 감지 작동 안하는 것 확인
            member.setUsername("username2");
            assertFalse(((Session) entityManager).isDirty());
            entityManager.flush();

            //동일성 보장 안되는 것 확인
            Member findMember = entityManager.find(Member.class, member.getId());
            assertNotEquals(member, findMember);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }

    /**
     * 식별자가 존재하고 영속성 컨텍스트와 연결되어 있지만 데이터베이스에서 제거될 상태
     */
    @Test
    @DisplayName("삭제")
    public void removeTest() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            //비영속 상태
            Member member = Member.createMember("username");
            //영속 상태 : 영속 컨텍스트에 관리
            entityManager.persist(member);
            entityManager.flush();

            //삭제 확인
            entityManager.remove(member);
            entityManager.flush();

            //데이터 베이스 값 없는 것 확인
            Member findMember = entityManager.find(Member.class, member.getId());
            assertNull(findMember);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}
