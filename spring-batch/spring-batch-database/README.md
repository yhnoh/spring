### CursorItemReader 사용시 주의사항
---

> CursorItemReader를 사용하실때는 Database와 SocketTimeout을 충분히 큰 값으로 설정해야만 합니다. <br/>
> ***Cursor는 하나의 Connection으로 Batch가 끝날때까지 사용되기 때문에 Batch가 끝나기전에 Database와 어플리케이션의 Connection이 먼저 끊어질수 있습니다.
*** <br/>
> 그래서 Batch 수행 시간이 오래 걸리는 경우에는 PagingItemReader를 사용하시는게 낫습니다. <br/>
> Paging의 경우 한 페이지를 읽을때마다 Connection을 맺고 끊기 때문에 아무리 많은 데이터라도 타임아웃과 부하 없이 수행될 수 있습니다. <br/>
> https://jojoldu.tistory.com/336

#### 위 글을 토대로 JpaCursorItemReader에서 확인해보기
```java
public class JpaCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T>
		implements InitializingBean {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
    //...
    //...
	@Override
	@SuppressWarnings("unchecked")
	protected void doOpen() throws Exception {
		this.entityManager = this.entityManagerFactory.createEntityManager();
        //...
	}

	@Override
	protected T doRead() {
		return this.iterator.hasNext() ? this.iterator.next() : null;
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		super.update(executionContext);
		this.entityManager.clear();
	}

	@Override
	protected void doClose() {
		if (this.entityManager != null) {
			this.entityManager.close();
		}
	}
}
```
- ItemStream은 스텝의 상태를 주기적으로 저장하고, 상태에 따라 활용할 수 있는 인터페이스이다.
- `doOpen()` 메서드는 Step이 실행될 때 호출되며, entityManager를 사용할 수 있게 생성한다.
- `doClose()` 메서드는 Step이 종료될 때 호출되며, entityManager를 반환한다.
- 실제 코드를 확인해보니, Step이 종료되기 전까지 커넥션이 연결된 것을 확인할 수 있다.
  - 때문에 CursorItemReader를 사용할 때는 주의하여 사용하자. 

### Spring Batch @DataJpaTest 사용중 발견한 문제점
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
    - 테스트 코드에서 해당 에러가 발생하는 이유는 메인 클래스에서 `@EnableBatchProcessing`이 설정되면서 디폴트 트랜잭션을 `DefaultBatchConfigurer`에서 셋팅하고 `JpaTransactionManager`를 셋팅하지 않기 때문이다.
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