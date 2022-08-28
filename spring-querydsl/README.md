### Querydsl 사용 이유

---
JPA를 활용하여 **동적쿼리나 복접한 쿼리를 사용하는데 분명한 한계**가 존재한다.
> 1. 쿼리를 JPQL문법으로 작성을 해야한다.
> 2. JPQL 문법으로 작성하는 것은 결구에는 문자열을 활용하는 것이기 때문에 IDE가 문법오류를 찾기 힘들다.
> 3. 문자열로 작성하다 보면 해당 내용이 어떤 일을 하는지 이해하기 힘들어진다.


Querydsl을 활용하면 JPA 동적쿼리,복잡한 쿼리의 한계를 극복할 수 있다.
> 1. 쿼리를 문자가 아닌 자바 코드로 작성 가능하다.
> 2. 자바 코드로 작성하니 문법 오류를 IDE가 잡아준다.
> 3. 쿼리 내용을 이해하기 쉽게 작성할 수 있다.

### Querydsl 환경설정

---
>- org.springframework.boot = 2.6.11
>- gradle = 7.5 


1. Gradle 환경 설정https://tychejin.tistory.com/388)
```groovy
//...
dependencies {
    //...
    // Querydsl
    implementation 'com.querydsl:querydsl-jpa'
    // Querydsl JPAAnnotationProcessor 사용 지정
    annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa"
    // java.lang.NoClassDefFoundError(javax.annotation.Entity) 발생 대응
    annotationProcessor 'jakarta.persistence:jakarta.persistence-api'
    // java.lang.NoClassDefFoundError(javax.annotation.Generated) 발생 대응
    annotationProcessor 'jakarta.annotation:jakarta.annotation-api'
    //
}

// clean task 실행시 QClass 삭제
clean {
    delete file('src/main/generated') // 인텔리제이 Annotation processor 생성물 생성 위치
}

//...
```

2. Gradle Build 이후 프로젝트에서 ./build/generated/annotationProcessor..내에 Q클래스 생성 확인
3. Querydsl 빈 객체로 설정하기
```java
@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }
}
```
- Querydsl을 사용하려면 쿼리를 Build하기 위해서 JPAQueryFactory가 필요하다.
- Querydsl을 사용면 EntityManager를 통해서 질의를 한다.
- JPAQueryFactory를 bean으로 등록한 이유는 repository에서 필요할때마다 생성해서 쓰는게 아니라 바로 가져와서 사용한다.


>- [gradle 환경설정](https://tychejin.tistory.com/388)
>- [gradle 동작원리](https://kotlinworld.com/321)

### Querydsl 기본 사용

---

1. 프로젝트 빌드를 통해서 생성된 Q클래스를 생성한다.
   - Q클래스 인스턴스를 사용하는 2가지 방법
   ```java
   QMember qMember = new QMember("m");
   QMember qMember = QMember.member; 
   ```
   
2. JPAQueryFactory를 활용해 질의한다.
```java
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
```

### 검색 조건 쿼리

---

- 기본적인 검색 쿼리를 제공해준다.
   ```java
    qtype.field.eq("")
    qtype.field.ne("")
    qtype.field.in("", "", ...)
    qtype.field.notIn("", "", ...)
    qtype.field.between("", "")
    qtype.field.isNotNull()
    qtype.field.goe("", "") // >=
    qtype.field.gt("", "") // >=
    qtype.field.loe("", "") // <=
    qtype.field.lt("", "") // <
    qtype.field.like("")  // SQL like문과 동일
    qtype.field.contains("")  // like문의 '%문자열%'과 동일
    qtype.field.startWith("") // like문의 '문자열%'과 동일 
    
   ```
- and, or을 활용하여 검색조건을 추가할 수 있다.
   - 검색 조건은 메서드 체인으로도 연결할 수 있다.
   ```java
    Member findMember = queryFactory
        .selectFrom(member)
        .where(member.username.eq("member1")
        .and(member.age.eq(10)))
        .fetchOne();

   ```
- where절에 파라미터로 검색조건을 추가하면 and 조건이 추가된다.
- [연습](./src/test/java/com/example/querydsl/basic/SearchQuerydsl.java)

### 결과 조회

---
- Querydsl을 통해서 질을한 내용을 단건 또는 리스트로 가져올 수 있다.
   - fetch()
     - 리스트 조회, 데이터가 없으면 빈 리스트 반환
   - fetchOne()
     - 결과가 없으면 null을 반환
     - 결과가 둘 이상이면 com.querydsl.core.NonUniqueResultException 에러 발생
   - fetchFirst()
     - 가장 최상단의 하나만 조회
- [연습](./src/test/java/com/example/querydsl/basic/ResultQuerydsl.java)

### 정렬

---

- orderBy()를 활용해 정렬을 할 수 있다.
   ```java
    List<Member> members = queryFactory
        .selectFrom(member)
        .orderBy(member.username.desc().nullsFirst())
        .fetch();
   ```
- desc(), asc()를 통해서 정렬이 가능하다.
- nullLast(), nullFirst()를 이용해 null 데이터의 순서를 부여 가능하다.
- [연습](./src/test/java/com/example/querydsl/basic/SortQuerydsl.java)

### 페이징 처리

---

- offest()과 limit()을 활용하여 페이징처리를 손쉽게 할 수 있다.
- limit()는 페이지 사이즈이며, offset()은 선택한 페이지이다.
- offset()의 첫 페이지는 0부터 시작한다.

- [연습](./src/test/java/com/example/querydsl/basic/PagingQuerydsl.java)

### 집합

---

- SQL에서 제공해주는 기본 집합함수를 활용가능하다.
  - ex) 합계, 카운트, 최소, 최대...
  ```java
   List<Tuple> tuples = queryFactory
        .select(
            member.count(),
            member.age.sum(),                        
            member.age.avg(),
            member.age.max(),
            member.age.min()
        )
        .from(member)
        .fetch();
   ```
- 그룹화된 결과를 가져와주는 GroupBy()와 그룹화된 결과에 조건을 걸 수 있는 having()을 이용하여 결과를 가져올 수 있다.
   ```java
    List<Tuple> tuples = queryFactory
        .select(team.name, member.age.avg())
        .from(member)
        .join(member.team, team)
        .groupBy(team.name)
        .having(member.age.avg().gt(30))
        .fetch();
   ```
- [연습](./src/test/java/com/example/querydsl/basic/AggregationQuerydsl.java)
  
### 조인

---

- Querydsl 조인의 기본 문법은 첫번째 파라미터에 조인 대상을 지정하고, 두 번째 파리머티에 별칭을 사용한다.
    ```java
        join(조인 대상, 별칭으로 사용할 Q타입)
    ```
- inner join
    ```java
        List<Member> members = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
    
    ```
- outer join : left 조인 및 right join 제공해준다.
    ```java
        List<Tuple> tuples = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .fetch();
    
    ```  
- theta join : 연관관계가 없는 필드로 조인
    ```java
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
    
        List<Member> members = queryFactory.select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();
        
    ```
- on 절을 활용하여 join절에 필터링을 걸 수 있다.
    ```java
        List<Tuple> tuples = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();
    
    ```
  - inner join으로 사용할 경우에는 거의 where 절과 동일한 기능으로 제공된다.
  - 그러므로 inner join을 사용할때는 where절로 해결하는것이 더 코드를 이해하기 쉽다.
- on절을 이용해 연관관계가 없는 필드로 외부 조인
    ```java
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        
        List<Tuple> tuples = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();
    ```
  - 일반 조인과 다르게 on 조인의 경우 leftJoin() 함수 파라미터로 team 엔티티 하나만 들어간다.
  - on 절을 이용하여 관계없는 필드 끼리 조인을 진행한다.
- on절을 이용해 연관관계가 있는 필드로 외부 조인
    ```java
        List<Tuple> tuples = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team)
                .on(member.team.eq(team))
                .fetch();
    ```
  - 일반 조인과 다르게 on 조인의 경우 leftJoin() 함수 파라미터로 team 엔티티 하나만 들어간다.
  - 일반적인 외부 조인과 동일한 기능을 가진다.
  - SQL을 자주 사용하던 사람들은 위 형식처럼 사용하는 것이 더 익숙할 수 있다.
- 페치 조인
    ```java
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();
        
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).isTrue();
    ```
  - 페치조인은 SQL에서 제공해주는 기능이 아니다.
  - SQL 조인을 활용해서 **연관된 엔티티를 SQL 한번에 조회하는 기능**이다.
  - join(), leftJoin() 등 조인 기능 뒤에 fetchJoin()이라고 추가하면 된다.
- [연습](./src/test/java/com/example/querydsl/basic/JoinQuerydsl.java)
