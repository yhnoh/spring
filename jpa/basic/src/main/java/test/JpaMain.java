package test;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        //엔티티 매니저 설정
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaBook");
        EntityManager em = emf.createEntityManager();
        //트랜잭션 관리
        EntityTransaction tx = em.getTransaction();

        try{

            tx.begin();
            //비지니스 로직 실행
            login(em);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally {
            em.close();
            emf.close();
        }
    }

    private static void login(EntityManager em) {
        //삽입
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("노영호");
        member.setAge(31);
        em.persist(member);

        //수정
        member.setAge(20);

        //단건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println(findMember);

        //JPQL 활용한 전체 조회
        List<Member> members = em.createQuery("select m from Member m where m.id = 'id1'", Member.class)
                .getResultList();
        System.out.println("members.size = " + members.size());

        //삭제
        em.remove(member);

    }

}
