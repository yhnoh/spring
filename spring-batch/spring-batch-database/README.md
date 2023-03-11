


### Spring Batch @DataJpaTest 진행 도중 발생하는 문제점
---
- ***애플리케이션 설정을 따로 분리하지 않으면 불필요한 application context 까지 읽어들이게 된다.***
- application context를 잘못 읽어들이면 슬라이스 테스트 진행이 안되는 경우가 발생할 수 있다.
- 해당 예제에서 `@SpringBootApplication`이 있는 메인 클래스에 `@EnableBatchProcessing`을 붙이게 되면 테스트 시 어떤 문제가 있는지 확인할 수 있다.
  - ***메인 클래스***
      ```java
      @SpringBootApplication
      @EnableBatchProcessing
      public class SpringBatchDatabaseApplication {

          public static void main(String[] args) {
              SpringApplication.run(SpringBatchDatabaseApplication.class, args);
          }

      }
      ```
- ***테스트 클래스***
    ```java
    @DataJpaTest
    public class MemberJpaRepositoryTest {
        @Autowired
        private MemberJpaRepository memberJpaRepository;
        @Autowired
        private OrderJpaRepository orderJpaRepository;
        @PersistenceContext
        private EntityManager entityManager;


        @Test
        public void defaultBatchFetchSizeTest() {
            //...
            List<Member> saveMembers = memberJpaRepository.saveAll(members);

            //...
            orderJpaRepository.saveAll(orders);

            entityManager.flush();
            entityManager.clear();

            List<Member> findMembers = memberJpaRepository.findAll();

            findMembers.get(0).getOrders().get(0);
            findMembers.get(1).getOrders().get(1);
        }

    }
    ```
    - `javax.persistence.TransactionRequiredException: no transaction is in progress` 에러 발생
    - 해당 에러가 발생하는 이유는 메인 클래스에서 `@EnableBatchProcessing`이 설정되면서 디폴트 트랜잭션을 `DefaultBatchConfigurer`에서 셋팅하고 JpaTransactionManager를 셋팅하지 않기 때문이다.
    ```java
    public class DefaultBatchConfigurer implements BatchConfigurer {
        	@PostConstruct
	public void initialize() {
		try {
			if(dataSource == null) {
				logger.warn("No datasource was provided...using a Map based JobRepository");

				if(getTransactionManager() == null) {
					logger.warn("No transaction manager was provided, using a ResourcelessTransactionManager");
					this.transactionManager = new ResourcelessTransactionManager();
				}
                //...
			} else {
				this.jobRepository = createJobRepository();
                //...
			}

			this.jobLauncher = createJobLauncher();
		} catch (Exception e) {
			throw new BatchConfigurationException(e);
		}
	}
    ```
- 해당 에러를 해결할기 위해서는 `@EnableBatchProcessing`로 구성된 별도의 Config 클래스를 제작하면 된다.
  - ***메인 클래스***
    ```java
    @SpringBootApplication
    public class SpringBatchDatabaseApplication {

        public static void main(String[] args) {
            SpringApplication.run(SpringBatchDatabaseApplication.class, args);
        }

    }
    ```
  - ***Batch 구성 클래스***
    ```java
    @Configuration
    @EnableBatchProcessing
    public class BatchConfig {
    }
    ```
- `CustomTransactionManager` 를 제작하여 만드는 것도 하나의 방법이 될 수 될 수 있다.

> https://mangkyu.tistory.com/243 <br/>
> https://github.com/spring-projects/spring-boot/issues/10703