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