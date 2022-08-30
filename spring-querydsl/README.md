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
> - org.springframework.boot = 2.6.11
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
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
```

- Querydsl을 사용하려면 쿼리를 Build하기 위해서 JPAQueryFactory가 필요하다.
- Querydsl을 사용면 EntityManager를 통해서 질의를 한다.
- JPAQueryFactory를 bean으로 등록한 이유는 repository에서 필요할때마다 생성해서 쓰는게 아니라 바로 가져와서 사용한다.

> - [gradle 환경설정](https://tychejin.tistory.com/388)
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
void startQuerydsl(){
        //insert
        Member member=new Member("member1");
        entityManager.persist(member);

        //Querydsl의 Q 타입 사용
        QMember qMember=QMember.member;

        //JPAQueryFactory를 가져와서 질의
        JPAQueryFactory query=new JPAQueryFactory(entityManager);
        Member findMember=query.selectFrom(qMember)
        .fetchOne();

        Assertions.assertEquals(1,findMember.getId());
        Assertions.assertEquals("member1",findMember.getUsername());

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

### 서브 쿼리

---

- where 절에 서브쿼리 사용
  ```java
    QMember memberSub = new QMember("memberSub");
    
    List<Member> members = queryFactory
            .selectFrom(member)
            .where(member.age.eq(
                    JPAExpressions
                            .select(memberSub.age.max())
                            .from(memberSub)
            ))
            .fetch();
  
  ```
- select 절에 서브쿼리 사용
  ```java
    QMember memberSub = new QMember("memberSub");
    
    List<Tuple> tuples = queryFactory
            .select(member.username,
                    JPAExpressions
                            .select(memberSub.age.avg())
                            .from(memberSub)
            )
            .from(member)
            .fetch();
  
  ```
- com.querydsl.jpa.JPAExpressions를 사용한다.
- JPQL은 from절의 서브쿼리를 지원하지 않는다. 그러므로 Querydsl도 지원하지 않는다.
- from 절의 서브쿼리 해결 방안
	1. 서브쿼리를 join으로 변경한다. (가능한 상황도 있고 불가능한 상황도 있다. )
	2. 애플리케이션에서 쿼리를 2번 분리한다.
	3. nativeSQL을 사용한다.
- [연습](./src/test/java/com/example/querydsl/basic/SubQuerydsl.java)

### Case문

---

- Q타입 클래스 필드를 활용한 case문
  ```java
    List<String> list = queryFactory
            .select(member.age
                    .when(10).then("열살")
                    .when(20).then("스무살")
                    .otherwise("기타"))
            .from(member)
            .fetch();
  ```
- CaseBuilder를 활용한 case문 활용
  ```java
    List<String> list = queryFactory
            .select(new CaseBuilder()
                    .when(member.age.between(0, 20)).then("0~20살")
                    .when(member.age.between(21, 30)).then("21~30살")
                    .otherwise("기타"))
            .from(member)
            .fetch();
  ```
- case문을 활용하여 우선순위 정하기
	- orderBy절을 활용한다.
  ```java
    NumberExpression<Integer> ageRank = new CaseBuilder()
            .when(member.age.between(0, 20)).then(2)
            .when(member.age.between(21, 30)).then(1)
            .otherwise(3);
    
    List<Tuple> tuples = queryFactory
            .select(member.username, member.age, ageRank)
            .from(member)
            .orderBy(ageRank.desc())
            .fetch();
  ```
- [연습](./src/test/java/com/example/querydsl/basic/CaseQuerydsl.java)

### Querydsl 함수 활용

---

- com.querydsl.core.types.dsl.Expressions 을 활용하여 Q타입내에서의 필드가 아닌, 자체 필드를 구성할 수 있다.
	- select 절에 상수 사용
  ```java
    Tuple tuple = queryFactory
            .select(member.username, Expressions.constant("A"))
            .from(member)
            .fetchFirst();
  ```
- Q타입 필드를 활용하여 내장 함수를 사용할 수 있다.
	- 문자열 더하기
  ```java
  String username = queryFactory
          .select(member.username.concat("_").concat(member.age.stringValue()))
          .from(member)
          .where(member.username.eq("member1"))
          .fetchOne();
  
  ```
- [연습](./src/test/java/com/example/querydsl/basic/FunctionQuerydsl.java)

### 프로젝션 : select 대상 지정

---

- 프로젝션 대상이 하나면 타입을 명확하게 지정하여 반환할 수 있다.
- 프로젝션 대상이 둘 이상이면 튜플이나 DTO로 반환할 수 있다.
- 프로젝션 대상이 하나일 경우
  ```java
    List<String> usernames = queryFactory
            .select(member.username)
            .from(member)
            .fetch();
  ```
- 프로젝션 대상이 둘 이상일 때 튜플로 반환
  ```java
    List<Tuple> tuples = queryFactory
            .select(member.username, member.age)
            .from(member)
            .fetch();
    
    for (Tuple tuple : tuples) {
        String username = tuple.get(member.username);
        Integer age = tuple.get(member.age);
    
        System.out.println("username = " + username + ", age = " + age);
    }
  
  ```
- 순수 JPA를 이용하여 DTO를 반환

  ```jpaql
    select new com.example.querydsl.entity.dto.MemberDto(m.username, m.age) from Member m
  ```

	- 패키지 명을 다 적어줘야 하기 때문에 불편하다.
	- 생성자 방식만 지원한다.
- Querydsl를 이용하여 DTO로 반환
	1. 프로퍼티 접근
		- DTO 클래스의 setter 메소드를 활용하여 DTO를 반환한다.
		- 기본 생성자 필요
		- setter 메서드를 이용할때는 dto 필드와 entity 필드가 일치해야한다.
	   ```java
		  List<MemberDto> members = queryFactory
				  .select(Projections.bean(MemberDto.class,
						  member.username,
						  member.age
				  ))
				  .from(member)
				  .fetch();
	   ```
	2. 필드 직접 접근
		- DTO클래스의 필드를 활용하여 DTO를 반환한다.
		- dto 필드와 entity 필드가 일치해야한다.
	   ```java
		List<MemberDto> members = queryFactory
			.select(Projections.fields(MemberDto.class,
					member.username,
					member.age
			))
			.from(member)
			.fetch();
	   ```
	3. dto 필드와 entity 필드가 다를 때 별칭 사용
		- 별칭을 사용하여 필드명을 일치시킨다.
		- 별칭은 `필드.as(alias)`, `ExpressionUtils.as(source, alias)`를 활용한다.
        ```java
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
        ```
  4. 생성자 사용
      - 생성자를 사용해 DTO로 반환할 수 있다.
      - 생성자 위치와 타입이 동일해야한다.
      ```java
      List<MemberDto> members = queryFactory
              .select(Projections.constructor(MemberDto.class,
                      member.username,
                      member.age
              ))
              .from(member)
              .fetch();
      ```
  5. @QueryProjection 활용
      - DTO 생성자에 @QueryProjection 어노테이션 추가한다.
      - maven 또는 gradle을 통해서 build하여 Q타입의 DTO를 생성한다.
      ```java
        List<MemberDto> members = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

      ```
      - 타입 체크를 할 수 있는 가장 안전한 방법이다.
      - 다만 DTO에 Querydsl 어노테이션을 유지해야하는 점과 DTO까지 Q파일을 생성해야하는 단점이 있다.
- [연습](./src/test/java/com/example/querydsl/basic/ProjectionQuerydsl.java)

### 동적 쿼리

---

- 검색을 할때 어떤 조건을 검색을 하거나 일부분은 필터링하고 검색하는 동적 쿼리가 필요하다.

- BooleanBuilder를 활용한 동적 쿼리
    ```java
    String usernameParam = "member1";
    Integer ageParam = 10;
    
    BooleanBuilder builder = new BooleanBuilder();
    if(usernameParam != null){
        builder.and(member.username.eq(usernameParam));
    }
    
    if(ageParam != null){
        builder.and(member.age.eq(ageParam));
    }
    
    List<Member> findMembers = queryFactory.selectFrom(member)
            .where(builder)
            .fetch();
    ```
    - `com.querydsl.core.BooleanBuilder`에 or절이나 and절 등을 활용하여 조건을 추가 삭제하 수 있다. 

- 다중 파라미터를 이용한 where절 동적 쿼리
    ```java
    String usernameParam = "member1";
    Integer ageParam = 10;
    
    BooleanExpression usernameEq = usernameParam != null ? member.username.eq(usernameParam) : null;
    BooleanExpression ageEq = ageParam != null ? member.age.eq(ageParam) : null;
    
    List<Member> findMembers = queryFactory.selectFrom(member)
            .where(usernameEq, ageEq)
            .fetch();
    ```
    - where 조건에 null 값은 무시하는 것을 활용하여 동적 쿼리를 사용할 수 있다.
    - BooleanExpression을 메소드로 추출하여 재활용을 할 수 있다.
    - 메소드 추출을 통하여 메소드에 네이밍을 줄 수 있으니 쿼리 자체에 대한 가독성이 높아진다.
    - 추출한 메소드들 끼리 and, or.. 메소드 체이닝을 통해서 조합이 가능하다.

### 사용자 정의 repository

---

- 사용자 정의 repository는 기존 Spring data JPA repository에 사용자가 직접 기능을 추가할 수 있는 기능이다.
- 사용자 정의 레파지토리 만들기
    1. 사용자 정의 repository 인터페이스 만들기 및 구현 메소드 선언
        ```java
        public interface MemberRepositoryCustom {
        
            List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition);
        
            List<MemberTeamDto> searchByParameters(MemberSearchCondition condition);
        
            Page<MemberTeamDto> searchPaging(MemberSearchCondition condition, Pageable pageable);
        }
        ```
    2. 사용자 정의 repository 구현체 만들기
        ```java
        @RequiredArgsConstructor
        public class MemberRepositoryImpl implements MemberRepositoryCustom {
            private final JPAQueryFactory queryFactory;
        
            List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition){
                //...
            }
        
            List<MemberTeamDto> searchByParameters(MemberSearchCondition condition){
                //...
            }
        
            Page<MemberTeamDto> searchPaging(MemberSearchCondition condition, Pageable pageable){
                //...
            }
            
        }
        ```
        - 사용자 정의 repository의 구현체 이름은 `사용자 정의 repository + Impl`로 해준다.
        - 메서드를 오버라이딩 한 이후 기능을 구현한다.
    
  3. JpaRepository 인터페이스에 상속받은 인터페이스에 같이 상속해준다.
      ```java
      public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
      }
      ```
  > 사용자 정의 repository를 기존 **JPA rpository에 추가하여 하고자 하는 로직(JPA, Querydsl, Mybatis)을 분리함과 동시에 인터페이스 하나로 통합하여 사용**할 수 있는 장점이 있다.  
  > 하지만 Querydsl이 필요한 경우는 보통 복잡한 쿼리, 비지니스 로직을 위한 쿼리, 화면을 위한 쿼리등 매우 복잡한 쿼리를 활용하는 경우가 많다.  
  > **특히 화면을 위한 쿼리는 화면이 변경되면 쿼리도 변경해야하고, 화면을 위한 기능도 많아지니 하나의 repository에 통합되어 있을 경우 해당 repository를 더 복잡하게 할 수 있다.**  
  > 따라서 항상 사용자 정의 repository를 통해 구현해야할 필요는 없다.
-[연습](./src/main/java/com/example/querydsl/repository)

