package com.example.persistencecontext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.*;

@SpringBootTest
public class MemberTest {


    @Test
    public void persistTest(){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        //엔티티 관리
        EntityManager em = emf.createEntityManager();
        //트랜잭션은 무모건 필요
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member member = null;
        try{
            //비영속 상태
            member = Member.createMember("id");
            //영속 상태 : 영속 컨텍스트에 관리
            System.out.println("====BEFORE====");
            em.persist(member);
            //System.out.println("member = " + em.member);
//            //준영속 상태 : 영속성 컨택스트에서 분리
//            em.detach(member);
//            em.remove(member);
            System.out.println("====AFTER====");

            tx.commit();
            //실제 DB에서 삭제
//            em.remove(member);
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    @Test
    public void persistTest2(){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        //엔티티 관리
        EntityManager em = emf.createEntityManager();
        //트랜잭션은 무모건 필요
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member member = null;
        try{
            //비영속 상태
            member = Member.createMember("id");
            //영속 상태 : 영속 컨텍스트에 관리
            System.out.println("====BEFORE====");
            em.persist(member);
            //System.out.println("member = " + em.member);
//            //준영속 상태 : 영속성 컨택스트에서 분리
//            em.detach(member);
//            em.remove(member);
            System.out.println("====AFTER====");

            tx.commit();
            //실제 DB에서 삭제
//            em.remove(member);
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

}
