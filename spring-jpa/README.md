### Spring Data JPA
---

- Spring Data JPA는 스프링 애플리케이션 개발 시 데이터베이스 접근 기술 중에 하나이며 관계형 데이터베이스 엔티티들의 관계를 객체의 관점으로 매핑해주는 기술이다.
- 
- Spring Data JPA는 ORM 기술을 구현하기 위하여 많은 기술들을 추상화하여 사용하고 있다. 그중에서 JDBC -> Hibernamte -> JPA (Java Persistence )


### Spring Data JPA Architecture
![](./img/spring-data-jpa-architecture.png)

- 사용자가 Spring Data JPA를 이용하여 데이터베이스에 접근할 때 어떻게 데이터를 불러오는지 단계적으로 한번 알아보자.
  1. 사용자는 Spring Data JPA에서 제공하는 `org.springframework.data.repository.Repository` 인터페이스를 이용하여 데이터 쓰기/읽기를 요청한다.
  2. Repository 인터페이스를 구현한 구현체에서는 `javax.persistence.EntityManager`를 상속받은 `org.hibernate.Session` 인터페이스를 이용하여 JDBC에게 데이터 쓰기/읽기를 요청한다.
  3. `org.hibernate.Session`은 JDBC `java.sql.Connection`을 구성하고 있으며, 이를 이용하여 데이터베이스에 접근하여 사용자가 요청한 데이터 쓰기/읽기 작업을 수행하게 된다.
- Spring Data JPA 기술은 JPA, Hibernate, JDBC 를 추상화하여 만들어진 기술이다.
> 데이터베이스와 Hibernate, EntityManager에 대해서 몰라도 Spring Data JPA를 사용할 수 있지만 문제가 발생하였을 때 이를 해결하기 쉽지 않을 수 있다. 때문에 Spring Data JPA가 사용하는 기술에 대한 이해도를 높이는 것이 중요하다.
