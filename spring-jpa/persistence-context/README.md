

### 영속성 컨텍스트란?
---
> 영속성 컨텐스트란 ***엔티티를 영구 저장하는 환경***이라는 뜻이다. 애플리케이션과 데이터베이스 사이에서 객체를 보관하는 가상의 데이터베이스 같은 역할을 한다. 엔티티 매니저를 통해 엔티티를 저장하거나 조회하면 ***엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관하고 관리***한다.

### 영속성 컨텍스트의 상태
---

- 영속성 컨텍스트에는 총 네가지 상태가 존재한다.

#### 1. transient (비영속)

- 영속성 컨텍스트와 전혀 관계가 없는 상태이다.
- 인스턴스의 생성 했지만 식별자(@id)가 없다.
```java
    Member member = new Member();
```

#### 2. managed / persistent (영속)
- 영속성 컨텍스트에서 관리되는 상태이다.
- ORM 기능을 활용 가능한 상태이다.
- 영속 상태 만들기
    - `entityManager.persist(entity);`
    - `entityManager.find(Entity.class, id)`
```java
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
```
#### 3. detach (준영속)
- 영속성 컨텍스트에서 관리되다 더이상 관리하지 않는 상태이다.
- 한번 관리되었기 때문에 식별자(@id) 값을 가지고 있다.
- 준영속 상태 만들기
	- `entityManager.detach(entity);`
	- `entityManager.close();`
	- `entityManager.clear();`
```java
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
```
#### 4. remove (삭제)
- 물리적인 데이터베이스에서 삭제가 예약된 상태이다.
- 식별자가 존재하며 영속성 컨텍스트에서 관리가 된다.
- 삭제 상태 만들기
	- `entityManager.persist(member);`
```java
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
```

### 엔티티 매니저 생명 주기
---

![](./img/persistence-context-lifecycle.png)

> **Reference**
> - [Hibernate-Reference](https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html#pc)
> - [JPA 영속성 컨텍스트란?](https://velog.io/@neptunes032/JPA-%EC%98%81%EC%86%8D%EC%84%B1-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8%EB%9E%80)