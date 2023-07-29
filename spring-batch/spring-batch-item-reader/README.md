### ItemReader를 Bean으로 생성시 주의할 점

### AbstractItemCountingItemStreamItemReader
---


### 1. ItemStream
---

- Chunk 단위의 배치 처리에서 ItemReader와 ItemWriter를 읽고 쓰는 작업이 가능하다.
- 하지만 그 이외의 작업 처리가 불가능하므로 ItemStream 인터페이스를 활용하는 것이다.
    - 그 이외의 작업 종류에는 어떤 것들이 있을까?
        - 데이터를 읽기 위하여 리소스 초기화 작업
        - Chunk 단위의 읽고 쓰는 작업이 끝났을때 초기화 했던 리소스를 해제하는 작업
        - 데이터를 어디까지 읽고 쓰고 있는지 확인할 수 있는 작업
- ItemStream은 ItemReader와 ItemWriter가 할 수 없는 나머지 작업들을 위한 인터페이스들을 제공한다.
    ```java
    public interface ItemStream {

        void open(ExecutionContext executionContext) throws ItemStreamException;

        void update(ExecutionContext executionContext) throws ItemStreamException;

        void close() throws ItemStreamException;
    }
    ```
- ItemStream 실행 시점 확인하기
- open
    - 스텝이 실행될 때 Chunk 단위 작업을 위해 리소스를 초기화하는데 주로 사용된다.
    - 스텝이 실행될 때 해당 메서드를 실행한다.
- update
    - 스텝이 실행되는 동안 Chunk 단위 작업시작, 진행 상태를 체크한다.
    - `ExcutionContext`를 통해서 현재 작업 상태를 저장소에 저장 가능하며, 해당 저장소에서 확인할 수 있다.
    - 스텝이 실행될때, 그리고 Chunk단위로 실행되며, 스텝이 종료되기 전에도 실행된다.
- close
    - 스텝이 종료될 때 Chunk 단위 작업이 끝난 이후 리소스를 해제한다.
    - 스텝이 종료될때 실행된다.

#### ItemStream을 확인하기 위한 간단한 예제

- 1부터 5까지 읽는 간단한 배치 프로그램을 제작

```java

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ItemStreamFlowJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public static final String JOB_NAME_PREFIX = "itemStreamFlow";

    @Bean(name = JOB_NAME_PREFIX + "Job")
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME_PREFIX + "Job").start(this.step()).build();
    }

    @Bean(name = JOB_NAME_PREFIX + "Step")
    public Step step() {
        return stepBuilderFactory.get(JOB_NAME_PREFIX + "Step")
                .<String, String>chunk(1)
                .reader(new ItemStreamFlowItemReader())
                .writer(items -> items.forEach(item -> log.info("write = {}", item)))
                .build();
    }

    @Slf4j
    public static class ItemStreamFlowItemReader implements ItemStreamReader<String> {

        private Iterator<String> iterator = List.of("1", "2", "3", "4", "5").iterator();
        String read = "";

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            read = iterator.hasNext() ? iterator.next() : null;
            log.info("read = {}", read);
            return read;
        }

        @Override
        public void open(ExecutionContext executionContext) throws ItemStreamException {
            log.info("open");
        }

        @Override
        public void update(ExecutionContext executionContext) throws ItemStreamException {
            log.info("update = {}", read);
        }

        @Override
        public void close() throws ItemStreamException {
            log.info("close");
        }
    }

}
```

- 결과

```text
Job: [SimpleJob: [name=itemStreamFlowJob]] launched with the following parameters: [{random=-8943712583433438564}]
Executing step: [itemStreamFlowStep]
open
update = 
read = 1
write = 1
update = 1
read = 2
write = 2
update = 2
read = 3
write = 3
update = 3
read = 4
write = 4
update = 4
read = 5
write = 5
update = 5
read = null
update = null
Step: [itemStreamFlowStep] executed in 39ms
close
Job: [SimpleJob: [name=itemStreamFlowJob]] completed with the following parameters: [{random=-8943712583433438564}] and the following status: [COMPLETED] in 59ms
```

- 잡과 스텝이 시작되면서 open, update 메서드를 호출하게 된다.
- 이후 chunkSize 만큼 read,write를 하고 완료될 경우 update 메서드를 호출한다.
- read에서 더이상 데이터를 읽을게 없을 경우 update 메서드를 실행하고, 스텝이 종료 이후 close 메서드를 호출한다.

#### ItemStream은 주로 어디서 사용될까?

- 가장 대표적으로 실패한 잡을 재실행할 때, 실패지점부터 다시 잡을 실행하도록 사용된다.
- 스텝에서 정크 단위로 작업이 완료될때마다 ItemStream의 update 메서드가 실행되면서, 해당 정크 단위의 작업의 상태를 쉽게 알 수 있다.
- 해당 ItemStream을 직접 구현도 할 수 있지만 스프링 배치에서 제공해주는 `AbstractItemCountingItemStreamItemReader` 클래스가 존재한다.
  - 해당 클래스는 스프링 배치 내에 존재하는 많은 ItemReader들이 상속받아 사용받고 있기 때문에 해당 클래스를 분석해보는 것이 좋다.
- 해당 클래스를 상속받아 내가 구현해야하는 배치 로직에 집중할 수 있는 장점이 있다.

```java
public abstract class AbstractItemCountingItemStreamItemReader<T> extends AbstractItemStreamItemReader<T> {
	private static final String READ_COUNT = "read.count";

	private static final String READ_COUNT_MAX = "read.count.max";

	private int currentItemCount = 0;

	private int maxItemCount = Integer.MAX_VALUE;

	private boolean saveState = true;
    
    //특정 시작 시점을 설정할 수 있는 로직
    @Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		super.open(executionContext);
		try {
			doOpen();
		}
		catch (Exception e) {
			throw new ItemStreamException("Failed to initialize the reader", e);
		}
		if (!isSaveState()) {
			return;
		}
        
		if (executionContext.containsKey(getExecutionContextKey(READ_COUNT_MAX))) {
			maxItemCount = executionContext.getInt(getExecutionContextKey(READ_COUNT_MAX));
		}

		int itemCount = 0;
		if (executionContext.containsKey(getExecutionContextKey(READ_COUNT))) {
			itemCount = executionContext.getInt(getExecutionContextKey(READ_COUNT));
		}
		else if(currentItemCount > 0) {
			itemCount = currentItemCount;
		}

		if (itemCount > 0 && itemCount < maxItemCount) {
			try {
				jumpToItem(itemCount);
			}
			catch (Exception e) {
				throw new ItemStreamException("Could not move to stored position on restart", e);
			}
		}

		currentItemCount = itemCount;

	}
    //update 메서드가 호출될 때마다 읽은 개수를 저장소에 저장
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		super.update(executionContext);
		if (saveState) {
            //
			Assert.notNull(executionContext, "ExecutionContext must not be null");
			executionContext.putInt(getExecutionContextKey(READ_COUNT), currentItemCount);
			if (maxItemCount < Integer.MAX_VALUE) {
				executionContext.putInt(getExecutionContextKey(READ_COUNT_MAX), maxItemCount);
			}
		}

	}
}
```

> https://jgrammer.tistory.com/entry/Spring-Batch-%EC%8B%A4%ED%8C%A8%EB%A5%BC-%EB%8B%A4%EB%A3%A8%EB%8A%94-%EA%B8%B0%EC%88%A0-ItemStream

### ItemReader

- ItemReader는 read 메서드를 통해서 값을 리턴 받거나 null을 리턴받는다.
    - 더이상 읽어들일 데이터가 없으면 null을 리턴한다.

```java
public interface ItemReader<T> {

    T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException;

}
```

### CursorItemReader

---

#### 작동원리

![](img/cursor_itemreader.png)

1. doOpen() 메서드를 통해서 쿼리 요청 및 데이를 받는다. (Connection Open)
2. `ItemReader`에서 read()메서드를 호출하며 cursor에서 데이터를 가져온다.
    - fetchSize 설정
        - fetchSize 만큼 데이터베이스에서 데이터를 응답받기 때문에 더이상 읽을게 없을경우 fetchSize만큼 데이터베이스에서 데이터를 읽어들인다.
        - 만약 fetchSize를 너무 크게 잡으면 데이터양이 많기 때문에 한번에 많은 데이터가 네트워크를 통해서 들어올 수 있기 때문에 느려질 수 있다. 때문에 적당한 fetchSize를 잡는 것이
          좋다. <br/>
          > https://dataonair.or.kr/db-tech-reference/d-guide/sql/?mod=document&uid=359
    - `JpaCursorItemReader`의 경우 fetchSize 설정이 없다. 많은 양의 데이터를 처리할 때 함부로 사용하지 않는 것이 좋다.
3. cursor를 통해서 더이상 읽을 데이터가 없을 doClose()메서드를 호출한 이후 Step을 종료한다. (Connection Close)

> `AbstractCursorItemReader` 참고하기 <br/>
> `JpaCursorItemReader, HibernateCursorItemReader`의 경우에는 `AbstractCursorItemReader`을 상속받지 않는다.<br/>

#### 장점

- Connection을 Step이 실행하는 동안 한번 맺고 끊고, Cursor설정을 하여 이동하는 방식이므로 높은 성능을 보여준다.
- Connection을 Step이 실행하는 동안 한번 맺고 끊기 때문에 데이터 무결성이 유지된다.

#### 단점

- Step이 종료되기 전까지 읽어들인 데이터를 가지고 있기 때문에 메모리 사용량이 높다.
- Thread Safe하지 않기 때문에 멀티 쓰레드 환경에서 사용하기 힘들다.
    - `PagingItemReader`에는 `synchronized` 예약어 존재
- Connection을 Step이 실행하는 동안 한번 맺고 끊기 때문에 Timeout 설정이 필요할 수 있다.

> https://renuevo.github.io/spring/batch/spring-batch-chapter-3/




### PagingItemReader
---

#### 작동 원리

![](img/paging_itemreader.png)

1. `doReadPage` 메서드를 통해서 pageSize만큼 쿼리를 요청한다. (Connection open)
2. pageSize만큼의 데이터를 받고난 뒤에 Iterator를 반환한다.
3. chunkSize 만큼 ItemReader는 read를 한다.
4. 다시 1 ~ 3번 작업을 진행한다.
5. 1 ~ 3번 작업을 진행하면서 데이터가 없다면 더이상 읽어들이지 않는다.
6. 해당 작업을 진행하면서 `AbstractItemCountingItemStreamItemReader`에서 `ExecuteContext`를 통해서 Step의 진행사항을 알 수 있다.

- chunkSize와 pageSize가 동일하지 않는경우
    - chunkSize (10) > pageSize (5)일 경우
        - doReadPage를 두번 실행한다.
    - chunkSize (5) < pageSize (10)일 경우
        - chunkSize를 무시하고 pageSize만큼 read, write를 진행한다.
    - ***pageSize만큼 읽어들인 이후 Step에서 chunkSize만큼 읽어들이기를 원한다면 pageSize와 chunkSize의 값을 같게하는 것이 좋다.***

> `AbstractPagingItemReader` 참고하기

#### 장점

- Connection을 유지하는 방식이 아니기 때문에 Timeout 설정에 크게 주의할 필요 없습니다.
- 메모리 이슈에 대해서 크게 신경쓰지 않아도됩니다.
    - `doReadPage` 메서드에서 `results.clear()`를 호출하여 읽어들인 데이터를 계속해서 삭제하는 것을 확인할 수 있습니다.
- 위와 같은 이유때문에 많은 양의 데이터를 처리할 때 유리합니다.

#### 단점

- Connection을 지속적으로 close하다보니 CursorItemReader 방식보다 느릴 수 있습니다.
- Batch 프로세스 동작동안 데이터 무결성이 유지되지 않을 수 있습니다.
    - 실시간 데이터를 탐색하는 방식이기 대문에 중간에 데이터가 바뀔 수 있습니다.

> https://renuevo.github.io/spring/batch/spring-batch-chapter-3/