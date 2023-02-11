
### 외부 환경에 영향을 받는 테스트 코드 작성시 발생할 수 있는 문제점

---

- 테스트의 목적은 프로그램의 각 부분이 정확하게 동작하는지 확인하는 것이다.
  - 하나의 동작에도 여러 TestCase를 만들어 프로그래머가 의도한데로 동작하는지 확인한다.
- ***때문에 연산을 여러 번 적용해도 결과가 바뀌지 않는 것이 중요하다. (멱등성)***
  - 동일한 input = 동일한 output
- 멱등성을 유지하기 위해서 개발자는 외부 환경에 의존하지 않고, 가짜 객체(Mock Object)를 만들어 테스트를 진행한다.
- 외부 환경(Database, Message Queue...)에 영향을 받는 테스트의 경우 운영과 개발 환경이 아닌 격리된 환경에서 테스트를 진행을 한다.
  - Local : Local 테스트 시, 모든 개발자의 PC마다 환경을 맞춰야 하는 경우가 발생할 수 있으며 동일한 데이터를 가질거라는 보장이 없다.
  - In-Memory : 외부환경과 동일한 환경이 아니기 때문에 호환성 문제가 발생할 수 있다.
  - Embedded Library : 현재 내가 사용하고 있는 환경을 지원하지 않을 수 있다.
- 그렇다면 테스트 코드 작성 시, 자동으로 격리된 환경을 제공하며 호환 문제를 해결할 수 있는 방법은 없을까?

> [유닛 테스트란?](https://ko.wikipedia.org/wiki/%EC%9C%A0%EB%8B%9B_%ED%85%8C%EC%8A%A4%ED%8A%B8)

### Docker를 활용한 테스트 코드 작성시 발생할 수 있는 문제점

---

- 위와 같은 문제를 해결하기 위해서 주로 사용하는 도구가 Docker일 것이다. DSL(Domain Specific Language)을 작성하여 모든 개발자들에게 배포할 수 있으며 운영과 동일한 환경을 각 개발자들에게 제공해 줄 수 있다.
- 하지만 테스트 코드 작성 시 Docker를 활용하게 될 경우 발생하는 몇가지 문제가 있다.
  - 언제 컨테이너 시작과 중지를 해야할지?
  - Port는 어떻게 매핑할지, 만약 다른 개발자들이 Dockerfile, docker-compose.yml에 명시된 포트를 이미 사용하고 있다면 어떻게 할지?
  - 컨테이너가 실행되고 있는 상황에서 테스트 코드가 여러번 실행되면 쌓이는 데이터 들은 어떻게 할지? 컨테이너를 삭제하고 다시 진행해야하나?
  - 배포하기 전 테스트코드 실행 시, 컨테이너를 어떻게 셋팅할지?
- Docker 처럼 격리된 환경을 제공해 주며, 위와 같은 문제들을 해결하기위해 사용할 수 있는 것이 바로 Testcontainers다.

### Testcontainers for Java

---

- JUnit 테스트를 지원하는 Java 라이브러리로 Docker 컨테이너에서 실행할 수 있는 모든 항목의 경량 일회용 인스턴스를 제공한다.
- Docker를 통해 컨테이너를 실행시킬 수 있는 다양한 방법들을 지원한다.
  - docker-hub, Dockerfile, docker-compose
  - Testcontainers에서 지원하는 모듈도 존재 (여러가지 Database 지원)
- 테스트 코드 실행/종료에 맞춰 컨테이너 실행/종료를 어떻게 할지 결정할 수 있다.

### 스프링 부트 junit5 환경에서 Testcontainers 설정하기

---

```groovy
ext {
    set('testcontainersVersion', "1.17.6")
}

dependencies {
    //...

    //Testcontainers dependency
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.testcontainers:junit-jupiter'

}

dependencyManagement {
    imports {
        mavenBom "org.testcontainers:testcontainers-bom:${testcontainersVersion}"
    }
}
```


### Testcontainers 실행해보기

#### 1. Testcontainers mariadb module을 이용해 mariadb 컨테이너 실행



1. dependency 설정
```groovy
    //Testcontainers mariadb module
    testImplementation 'org.testcontainers:mariadb'
```
> - [Testcontainers mariadb module](https://www.testcontainers.org/modules/databases/mariadb/)


2. maraiadb 환경 설정
```yaml
## application-test.yml
spring:
  datasource:
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
    url: jdbc:tc:mariadb:10.3.8:///test
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

> - [JDBC 환경 설정](https://www.testcontainers.org/modules/databases/jdbc/)


1. `@Testcontainers`, `@Container` 어노테이션을 이용한 테스트 코드 작성
```java
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RestartContainerEachMethodTest {

    @Container
    private final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    public void setup(){
        Member member = Member.createMember("username", "password");
        memberJpaRepository.save(member);
    }

    @Test
    public void testCase1(){
        Member findMember = memberJpaRepository.findByUsername("username").get();
        assertThat(findMember.getUsername()).isEqualTo("username");
    }
}
```

- `testCase1` 실행시, mariadb 컨테이너가 실행 -> 테스트 코드 실행 및 종료 -> 컨테이너 종료
- `@Testcontainers` 어노테이션을 이용해 테스트 실행시, 컨테이너 실행과 종료를 자동으로 제어한다.

#### 2. docker-hub에서 이미지 가져와 Testcontainers 실행해보기 

1. Redis 설정
```java
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
```

2. Redis 테스트 해보기
```java
@Testcontainers
@Slf4j
@SpringBootTest(classes = RedisConfig.class)
@ActiveProfiles("test")
public class GenericContainerTest {


    /**
     * .withExposedPorts(6379) 컨테이너 관점에서의 포트 번호
     * .getMappedPort(6379) 실제 호스트와 매핑된 포트 번호를 가져오는 방법
     *
     * 로컬에서 실행 중인 소프트웨어와의 포트 충돌을 방지하기 위해서 이런식으로 설계 되었다.
     */

    @Container
    private static final GenericContainer REDIS_CONTAINER = new GenericContainer("redis:7.0.8-alpine")
            .withExposedPorts(6379);

    @Autowired
    private RedisTemplate redisTemplate;
    private final String KEY="keyword";

    @DynamicPropertySource
    public static void setDynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));

    }

    @Test
    public void containerTest(){
        assertThat(REDIS_CONTAINER.isCreated()).isEqualTo(true);
        assertThat(REDIS_CONTAINER.isRunning()).isEqualTo(true);
    }

    @Test
    public void redisTest(){
        //given
        String keyword="한남동 맛집";
        String keyword2="서촌 맛집";

        //when
        redisTemplate.opsForZSet().add(KEY,keyword,1);
        redisTemplate.opsForZSet().incrementScore(KEY,keyword,1);
        redisTemplate.opsForZSet().incrementScore(KEY,keyword,1);

        redisTemplate.opsForZSet().add(KEY,keyword2,1);

        //then
        System.out.println(redisTemplate.opsForZSet().popMax(KEY));
        System.out.println(redisTemplate.opsForZSet().popMin(KEY));
    }
}
```

- `GenericContainer` 클래스를 통해서 도커허브에서 이미지를 가져와 컨테이너를 실행시킬 수 있다.
- `withExposedPorts(6379)` 컨테이너 관점에서의 포트이며, 호스트에서 실제 컨테이너가 돌아가는 포트에 접근하기 위해서 `getMappedPort(6379)`를 사용한다.
  - `getMappedPort(6379)`을 통해서 포트를 가져오게 되면 랜덤으로 포트가 설정되어 있다.
  - 로컬에서 실행 중인 소프트웨어와의 포트 충돌을 방지하기 위해서 이런식으로 설계 되었다. 

#### 3. docker-compose를 통해 Testcontainers 실행해보기

1. src/test/resources/docker/docker-compose.yml
```yaml
version: "3"

services:
  redis:
    image: redis
  elasticsearch:
    image: elasticsearch:8.6.1
```

2. docker-compose.yml 파일을 이용해 테스트 해보기

```java
@Testcontainers
public class DockerComposeTest {

    @Container
    private static DockerComposeContainer DOCKER_COMPOSE_CONTAINER;
    static {
        try {
            File dockerComposeFile = new ClassPathResource("./docker/docker-compose.yml").getFile();
            DOCKER_COMPOSE_CONTAINER = new DockerComposeContainer(dockerComposeFile)
                    .withExposedService("redis", 6379)
                    .withExposedService("elasticsearch", 9200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void dockerComposeTest(){

        ContainerState redis = (ContainerState) DOCKER_COMPOSE_CONTAINER.getContainerByServiceName("redis").get();
        ContainerState elasticsearch = (ContainerState) DOCKER_COMPOSE_CONTAINER.getContainerByServiceName("elasticsearch").get();

        Assertions.assertThat(redis.isRunning()).isTrue();
        Assertions.assertThat(elasticsearch.isRunning()).isTrue();

        int redisPort = redis.getMappedPort(6379);
        int elasticsearchPort = elasticsearch.getMappedPort(9200);
        System.out.println("redisPort = " + redisPort);
        System.out.println("elasticsearchPort = " + elasticsearchPort);
    }

}
```

- `DockerComposeContainer` 클래스를 통해서 docker-compose 경로를 셋팅하여 컨테이너를 실행시킬 수 있다.
- `withExposedService("redis", 6379)` 를 통해 어떤 컨테이너를 실행시킬지 선택할 수 있다.
- `getContainerByServiceName("redis")` 를 통해 해당 컨테이너를 가져올 수 있다.

### Testcontainers 생명주기

#### 1. Restarted containers
- 인스턴스 필드를 선언하면 각 테스트 메서드 별로 컨테이너 시작과 중지를 한다.

```java
@Container
private final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");
```

- 컨테이너 시작과 중지를 각 메서드별로 실행할 경우 컨테이너를 실행시키는 시간 때문에 테스트가 느려질 수 있다.

#### 2. Shared containers
- 정적 필드로 선언된 컨테이너는 해당 클래스 내에서 테스트 메서드 간에 공유되며, 한번만 실행 및 중지를 한다.

```java
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SharedContainerAllMethodTest {

    @Container
    private static final MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @BeforeEach
    public void setup(){
        Member member = Member.createMember("username", "password");
        memberJpaRepository.save(member);
    }

    @Test
    @DisplayName("데이터 공유 확인")
    public void shareContainerVolume1Test(){
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("데이터 공유 확인")
    public void shareContainerVolume2Test(){
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(1);
    }
}
```
-  ***"하나의 클래스내에서 컨테이너가 공유가 되니 데이터도 공유가 되는 것이 아닌가?" 하는 의문이 들 수는 있겠지만 실제로 데이터는 공유되지 않는다.***

#### 3. Singleton containers

```java
public class AbstractSingletonContainerTest {

    static final MariaDBContainer MARIA_DB_CONTAINER;
    static {
        MARIA_DB_CONTAINER = new MariaDBContainer("mariadb:10.3.8");
        MARIA_DB_CONTAINER.start();
    }
}
```
- 여러 테스트 클래스에 대한 딱 한번만 시작되는 컨테이너를 정의하고자 할때 사용할 수 있다.
- 해당 방식은 Testcontainers에서 특별한 지원을 해주는 것이 없다. ex) `@Testcontainers`
- `AbstractSingletonContainerTest` 해당 클래스를 상속받아 테스트를 진행할 수 있다.
- `Shared containers`와 같이 데이터를 서로 공유하지는 않는다.

> Reference <br/>
> - [https://www.testcontainers.org/](https://www.testcontainers.org/) <br/>
> - [https://dealicious-inc.github.io/2022/01/10/test-containers.html](https://dealicious-inc.github.io/2022/01/10/test-containers.html)