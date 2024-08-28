### 1. Spring Batch Multi DataSource

- Spring Batch Job을 실행하는 도중에 여러 데이터베이스에 연결하여 Job을 처리해야하는 경우가 발생하였다.
  > 마이크로서비스를 운영하며 데이터베이스를 서비스별로 잘 나누어 두었다면 상관없겠지만 모든 회사가 그렇게 대응할 수 없는 것도 현실이다.
- 위의 기능을 수행하기 위한 필요 사항
    - Multi DataSource 설정
    - Multi DataSoruce에 따른 JPA 설정

#### 1.1. MultiDataSource 설정

```java

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    public static final String MEMBER_DATA_SOURCE_BEAN_NAME = "memberDataSource";
    public static final String ORDER_DATA_SOURCE_BEAN_NAME = "orderDataSource";

    //...


    private HikariDataSource createDataSource(DataSourceProperties hikariDataSourceProperties,
            JdbcConnectionProperties memberJdbcConnectionProperties) {
        HikariDataSource dataSource =
                DataSourceBuilder.create(hikariDataSourceProperties.getClassLoader()).type(HikariDataSource.class)
                        .driverClassName(memberJdbcConnectionProperties.getDriverClassName())
                        .url(memberJdbcConnectionProperties.getUrl())
                        .username(memberJdbcConnectionProperties.getUsername())
                        .password(memberJdbcConnectionProperties.getPassword()).build();

        dataSource.setPoolName(memberJdbcConnectionProperties.getHikari().getPoolName());
        return dataSource;
    }
}
```

- [자세한 코드](./src/main/java/org/example/springbatchmultidatasource/jpa/DataSourceConfig.java)
- DataSoruce는 HikariDataSource를 사용하고 해당 객체를 생성하기 위해서는 `createDataSoruce` 메서드를 만들었다.
    - `DataSourceConfiguration` 클래스를 참고하여 DataStocue 생성 메서드 작성
- `createDataSoruce` 메서드를 이용하여 회원과 주문 데이터소스를 생성하고, @Primary는 회원 데이터 소스에 설정해두었다.
  > Spring Batch 에서는 Job이나 Step의 실행 상태를 확인할 수 있는 MetaData가 존재한다. MetaData의 스키마는 데이터베이스에 셋팅이 가능하며 이를 위해서는 하나의 데이터소스가
  설정되어야한다.<br/>
  > [Sptring Batch > Meta-Data Schema](https://docs.spring.io/spring-batch/reference/schema-appendix.html)<br/>
  > Multi DataSoruce 설정시에는 데이터소스가 두개이상이 되기 때문에, @Primary 어노테이션을 이용하여 어떤 데이터베이스에 Meta Data를 설정할지를 결정한다. <br/>
  > 만약 다른 데이터베이스를 셋팅하고 싶다면, `JobRepository`를 재설정하면 된다.

#### 1.2. Multi DataSoruce에 따른 JPA 설정

```java

@RequiredArgsConstructor
public class JpaConfigFactory {

    private final DataSource dataSource;
    private final JpaProperties jpaProperties;
    private final String[] packagesToScan;


    public JpaVendorAdapter createJpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(this.jpaProperties.isShowSql());
        if(this.jpaProperties.getDatabase() != null) {
            hibernateJpaVendorAdapter.setDatabase(this.jpaProperties.getDatabase());
        }
        if(this.jpaProperties.getDatabasePlatform() != null) {
            hibernateJpaVendorAdapter.setDatabasePlatform(this.jpaProperties.getDatabasePlatform());
        }
        hibernateJpaVendorAdapter.setGenerateDdl(this.jpaProperties.isGenerateDdl());
        return hibernateJpaVendorAdapter;
    }


    public LocalContainerEntityManagerFactoryBean createEntityManagerFactory(JpaVendorAdapter jpaVendorAdapter) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(packagesToScan);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaPropertyMap(jpaProperties.getProperties());
        return entityManagerFactory;
    }


    public PlatformTransactionManager createTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
```

- [자세한 코드](./src/main/java/org/example/springbatchmultidatasource/jpa/JpaConfigFactory.java)
    - [회원 JPA 설정 코드](./src/main/java/org/example/springbatchmultidatasource/jpa/MemberJpaConfig.java)
    - [주문 JPA 설정 코드](./src/main/java/org/example/springbatchmultidatasource/jpa/OrderJpaConfig.java)
- Jpa설정 정보는 `JpaBaseConfiguration` 클래스를 참고하여 제작하였고, 이를 이용하여 회원과 주문 JPA 설정 정보를 추가하였다.

### 회원별 총 주문 수 코드 작성

- Spring Batch에 Multi DataSource를 설정한 이후 실제 코드를 한번 작성해보자.
- 기능 요구사항은 아래와 같다.
    - 한명의 회원당 총 몇 건의 주문을 했는지를 알고자한다.
    - 주문에 대한 모든 정보는 주문 데이터베이스의 주문 테이블에서 정보를 가져온다.
    - 가져온 주문 정보를 가지고 회원 데이터베이스의 회원 테이블에 총 주문수를 셋팅한다.
    - member1에 총 주문 수는 10건이며, member2의 총 주문수는 5건으로 미리 데이터를 셋팅해둔다.
        - [MemberOrderCountDataInitialization.java](./src/main/java/org/example/springbatchmultidatasource/MemberOrderCountDataInitialization.java)

#### 2.1. JOB 설정

```java

@RequiredArgsConstructor
@Configuration
public class MemberOrderCountJobConfig {

    public static final int CHUNK_SIZE = 1;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MemberOrderCountItemWriter memberOrderCountItemWriter;

    private final MemberService memberService;


    @Bean
    public Job memberOrderCountJob() {
        return new JobBuilder("memberOrderCountJob", jobRepository).start(this.memberOrderCountStep())
                .listener(new JobExecutionListener() {

                    @Override
                    public void beforeJob(JobExecution jobExecution) {

                    }


                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        List<MemberJpaEntity> memberJpaEntities = memberService.findAll();
                        for(MemberJpaEntity memberJpaEntity : memberJpaEntities) {
                            System.out.println("memberJpaEntity = " + memberJpaEntity);
                        }
                    }
                }).build();
    }


    @Bean
    public Step memberOrderCountStep() {
        return new StepBuilder("memberOrderCountStep", jobRepository).<MemberJpaEntity, MemberJpaEntity>chunk(
                        CHUNK_SIZE, platformTransactionManager).reader(this.memberOrderCountItemReader(null))
                .writer(memberOrderCountItemWriter).build();
    }


    @Bean
    public JpaPagingItemReader<MemberJpaEntity> memberOrderCountItemReader(
            @Qualifier("memberEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<MemberJpaEntity>().name("memberOrderCountItemReader")
                .entityManagerFactory(entityManagerFactory).queryString("select m from MemberJpaEntity m")
                .pageSize(CHUNK_SIZE).build();
    }
}
```

- `memberOrderCountItemReader`는 회원의 정보를 한명씩 가져오는 역할을 한다.
- `memberOrderCountItemWriter`는 회원 정보를 기반으로 주문 데이터베이스에서 총 주문수를 가져와 회원 정보에 셋팅하는 역할을 한다.
- Job이 완료된 이후에는 `JobExecutionListener`를 이용하여 회원별 총 주문수가 잘 셋팅되었는지 확인한다.

#### 2.2. ItemWriter 설정

```java

@Component
@RequiredArgsConstructor
public class MemberOrderCountItemWriter implements ItemWriter<MemberJpaEntity> {

    private final OrderService orderService;
    private final ItemWriter<MemberJpaEntity> memberJpaItemWriter;


    @Override
    public void write(Chunk<? extends MemberJpaEntity> chunk) throws Exception {
        List<Long> memberIds = chunk.getItems().stream().map(MemberJpaEntity::getId).collect(Collectors.toList());
        List<OrderJpaEntity> orderJpaEntities = orderService.findAllByMemberIdIn(memberIds);
        Map<Long, List<OrderJpaEntity>> orderJpaEntityMap =
                orderJpaEntities.stream().collect(Collectors.groupingBy(OrderJpaEntity::getMemberId));

        for(MemberJpaEntity memberJpaEntity : chunk) {
            int orderCount = orderJpaEntityMap.containsKey(memberJpaEntity.getId()) ?
                    orderJpaEntityMap.get(memberJpaEntity.getId()).size() :
                    0;
            memberJpaEntity.changeOrderCount(orderCount);
        }

        memberJpaItemWriter.write(chunk);
    }
}
```

- `MemberOrderCountItemWriter`는 아래와 같은 역할을 한다.
    - 아이템 리더에서 읽어들인 회원을 파라미터로 입력받는다.
    - 회원 아이디를 이용하여 회원별 총 주문을 가져온다.
    - 가져온 주문정보를 회원별 총 주문수로 가공하여 회원 정보에 셋한다.
- 회원별 총 주문수 잡을 실행하면 정상동작하는 것을 확인할 수 있다.
  ```text
  memberJpaEntity = MemberJpaEntity(id=1, name=member1, orderCount=10)
  memberJpaEntity = MemberJpaEntity(id=2, name=member2, orderCount=5)
  ```

### 3. 결론

- Spring Batch 작업을 진행하다보면 Multi DataSource를 설정하여 작업을 해야하는 경우가 발생할 수 있으며, 이를 대비하는 예제를 한번 만들어보았다.
- 뿐만아니라 하나의 Job에서 Multi DataSource를 활용하여 정보를 조합하고 이를 저장할 수 있는 것도 예제코드를 통해서 확인할 수 있다.
- 인터넷에서 찾는 자료는 단순한 로직들 대상으로 Job을 처리하는 경우가 많은데, 단순한 로직 뿐만 아니라 복잡한 로직도 Spring Batch를 이용하여 처리 가능하다는 것을 확인할 수 있다.
