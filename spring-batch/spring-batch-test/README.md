### 스프링 배치 테스트 코드 필요성
---

- 배치 어플리케이션은 UI가 사용자들에게 직접적으로 제공되지 않을 수 있기 때문에 ***QA를 개발자가 직접 진행***해야한다.
- 때문에 테스트 코드를 통해서 개발자가 직접 검증해보는 것이 좋다.
  - 작성된 로직이 개발자가 의도한 대로 동작하는지 확인 
  - 개발자가 원하는 저장소에 데이터가 잘 셋팅이 되는지 확인
  - 등등..
- 배치의 경우 외부 환경 셋팅에 맞춰서 검증해주는 것이 좀 더 효율적일 수 있다. 때문에 테스트 코드를 돌릴 환경을 쉽게 셋팅해줄 수 있는 장치들을 마련해주는 것이 좋다.
  - 로컬, 베타, 알파, 개발 환경 등등... 
> https://jojoldu.tistory.com/455

### 스프링 배치 테스트 코드 셋팅 전 알아야할 것
1. @SpringBatchTest
   - 스프링 배치 4.1에서 새롭게 제공되는 어노테이션
   - `org.springframework.batch:spring-batch-test` 라이브러리에서 제공
   - @SpringBatchTest는 스프링 배치에서 사용하는 빈들을 ApplicationContext에 자동으로 등록해주어 테스트할 수 있도록 많은 유틸리티를 제공해준다.
     - JobLauncherTestUtils: test환경에서 job과 step을 실행할 수 있다.
     - JobRepositoryTestUtils: test환경에서 JobRepository에서 JobExecutions를 생성하거나 제거하는데 사용된다. (스프링 배치 메타 데이터와 관련)
     - StepScopeTestExecutionLitener, JobScopeTestExecutionLitener: 테스트 환경에서 StepScope와 JobScope 빈 사용할 수 있다.
    > [SpringBatchTest](https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/test/context/SpringBatchTest.html)
2. @SpringBootTest
   - 
3. @EnableBatchProcessing

### 스프링 배치 테스트 코드 작성전 환경 셋팅

#### 1. build.gradle
---
```groovy
ext {
    set('testcontainersVersion', "1.17.6")
}

dependencies {
    //...
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.batch:spring-batch-test'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:mariadb'
}

dependencyManagement {
    imports {
        mavenBom "org.testcontainers:testcontainers-bom:${testcontainersVersion}"
    }
}

```

- `org.springframework.batch:spring-batch-test`를 통해서 스프링 배치 테스트를 작성할 수 있다.
- spring embedded database(h2)가 아닌 testcontainers 라이브러리를 활용하여 mariadb 데이터 베이스를 셋팅한다.
  - 실제 운영중인 데이터베이스와 일치하여 사용할 수 있기 때문에 테스트시 유용하다.
  - docker를 통해서 데이터베이스를 셋팅해 줌으로 docker는 설치되어야 한다.

#### application-test.yml
---
```yaml
spring:
  datasource:
    url: jdbc:tc:mariadb:10.5.8:///spring_batch
    username: sa
    password:
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
  batch:
    job:
      names: ${job.names:NONE}
    jdbc:
      initialize-schema: always
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
```
- spring batch에는 batch관련 메타데이터를 저장하는 repository가 필요하기 때문에 해당 메타데이터를 셋팅하기 위해서 test 시에는 `spring.batch.jdbc.initialize-schema: always`로 셋팅해 준다.
  - always: 항상 메타데이터 초기화
  - embedded: embedded database인 경우에만 메타데이터 초기화
  - none: 메타데이터를 초기화하지 않음
  > [spring batch meta-data schema](https://docs.spring.io/spring-batch/docs/current/reference/html/schema-appendix.html#metaDataSchema)
- datasource의 경우 testcontainers를 사용할 예정이므로 testcontainers JDBC를 셋팅해준다.
  > [testcontainers jdbc](https://www.testcontainers.org/modules/databases/jdbc/)

#### TestBatchConfig

```java
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class TestBatchConfig {
}
```

- 테스트시 배치환경 및 설정 초기화를 자동 구성하기 위한 클래스


### 스프링 배치 테스트 코드 작성

#### 테스트 해보고자 하는 Job
- FlatFileItemReader를 통해서 파일을 읽어들여 데이터베이스에 저장을 하는 Job이다.
  - file의 경우 resource/data에 존재
  - database는 로컬 db를 사용
  - 테스트 환경에서는 testcontainers를 활용하여 database를 셋팅

#### 1. 통합 테스트
```java
@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, DataJobConfig.class})
@ActiveProfiles("test")
class DailJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private DailyStockPriceEntityRepository dailyStockPriceEntityRepository;

    @BeforeEach
    void beforeEach(){
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @BeforeEach
    void afterEach(){
        dailyStockPriceEntityRepository.deleteAll();
    }

    @Test
    void launchJobTest() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        assertThat(dailyStockPrices.size()).isEqualTo(99);
    }

    @Test
    void launchJobTest2() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        assertThat(dailyStockPrices.size()).isEqualTo(99);

    }

}
```

- @SpringBootTest에서 내가 테스트하고자 하는 Job을 선언한다.
- jobLauncherTestUtils을 주입받아 `launchJob()`메서드를 통해서 잡 실행
  - 해당 job을 성공 여부 확인
- jobRepositoryTestUtils을 주입받아 각 메서드별로 테스트 코드가 실행하기 전 `removeJobExecutions()` 메서드를 실행하여 repository에 존재하는 데이터 삭제
- 각 메서드별로 테스트 코드가 완료되면 insert하였던 데이터 삭제
- @SpringBootTest에서는 @Transactional이 없기 때문에 데이터 Rollback을 지원하지 않는다.
  - 때문에 데이터들을 수동으로 제거하는 작업 진행한다.
  - @Trasactional을 선언하면 어떻게되나?
    - `java.lang.IllegalStateException: Existing transaction detected in JobRepository. Please fix this and try again (e.g. remove @Transactional annotations from client).` 에러 발생
    - <span style="color:red">https://brunch.co.kr/@anonymdevoo/50 를 통해서 내용 확인해보기</span>

#### 2. 외부환경에 의존하지 않는 통합 테스트

```java
@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class, DataJobConfig.class})
@ActiveProfiles("test")
class DailJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @MockBean(name = "dataItemReader")
    private FlatFileItemReader<DailyStockPriceDTO> dataItemReader;

    @Autowired
    private DailyStockPriceEntityRepository dailyStockPriceEntityRepository;

    @BeforeEach
    void setup(){
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @AfterEach
    void afterEach(){
        dailyStockPriceEntityRepository.deleteAll();
    }

    @Test
    void launchJobTest() throws Exception {
        DailyStockPriceDTO dailyStockPriceDTO = DailyStockPriceDTO.builder()
                .marketDate(LocalDate.of(2023, 03, 04))
                .openPrice(BigDecimal.valueOf(1L))
                .highPrice(BigDecimal.valueOf(1L))
                .lowPrice(BigDecimal.valueOf(1L))
                .closePrice(BigDecimal.valueOf(1L))
                .volume(1L)
                .openInt(1)
                .build();

        given(dataItemReader.read()).willReturn(dailyStockPriceDTO, (DailyStockPriceDTO) null);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        assertThat(dailyStockPrices.size()).isEqualTo(1);
    }

    @Test
    void launchJobTest2() throws Exception {
        DailyStockPriceDTO dailyStockPriceDTO = DailyStockPriceDTO.builder()
                .marketDate(LocalDate.of(2023, 03, 04))
                .openPrice(BigDecimal.valueOf(1L))
                .highPrice(BigDecimal.valueOf(1L))
                .lowPrice(BigDecimal.valueOf(1L))
                .closePrice(BigDecimal.valueOf(1L))
                .volume(1L)
                .openInt(1)
                .build();

        given(dataItemReader.read()).willReturn(dailyStockPriceDTO, (DailyStockPriceDTO) null);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<DailyStockPrice> dailyStockPrices = dailyStockPriceEntityRepository.findAll();
        assertThat(dailyStockPrices.size()).isEqualTo(1);

    }

}
```
- JpaItemWriter의 경우 testcontainers 설정으로 외부환경에 의존받지않고 테스트 진행이 가능
- FlatFileItemReader의 경우에는 외부에서 파일을 전달받아 데이터를 읽어들일 수 있고 해당 파일 내용이 계속 변경될 수 있기 때문에 해당 테스트 시에 멱등성이 지켜지지 않을 있다.
  - 이를 해결하기 위해 @Mockbean을 사용하여 신뢰가능한 테스트 제작
  - `given(dataItemReader.read()).willReturn(dailyStockPriceDTO, (DailyStockPriceDTO) null);`
  - willReturn에서 마지막에 null 값을 넣은 이유는 read시 마지막에 null이 아니면 무한르프에 빠지게 된다.
    - itemReader가 데이터를 읽어들이는 조건을 잘 생각해보자. 