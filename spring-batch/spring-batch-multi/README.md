### Spring Batch 병렬 처리

- 많은 배치 프로세스가 단일 스레드, 단일 프로세스를 통해서 문제를 해결할 수 있지만 데이터 양이 많아질 수록 배치 처리 시간이 길어질 수 있다.
- 해당 문재를 해결하기 위하여 Spring Batch 프레임워크는 멀티 스레드 및 멀티 프로세스를 이용한 병렬 처리를 지원한다.

### 예제

```java
public class SimpleStepsJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job simpleStepsJob() {
        return new JobBuilder("simpleStepsJob", jobRepository)
                .start(this.step1())    //짝수를 출력하는 step
                .next(this.step2())     //홀수를 출력하는 step
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Integer, Integer>chunk(1, platformTransactionManager)
                .reader(this.itemReader1())
                .writer(this.itemWriter1())
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader1() {
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 0)
                .boxed()
                .collect(Collectors.toList());

        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<Integer> itemWriter1() {
        return items -> items.forEach(item -> log.info("step1 item = {}", item));
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .<Integer, Integer>chunk(1, platformTransactionManager)
                .reader(this.itemReader2())
                .writer(this.itemWriter2())
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader2() {
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 1)
                .boxed()
                .collect(Collectors.toList());
        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<Integer> itemWriter2() {
        return items -> items.forEach(item -> log.info("step2 item = {}", item));
    }

}
```

- simpleStepsJob은 1부터 100사이에서 짝수와 홀수를 구분하여 출력하는 Job이다.
- step1은 짝수를 출력하고, step2는 홀수를 출력하며 해당 Job은 단일 프로세스, 단일 스레드에서 작동을 하게 된다.

```text
2024-10-22T22:59:44.377+09:00  INFO 54057 --- [           main] o.e.s.step.SimpleStepsJobConfig          : step1 item = 100
2024-10-22T22:59:44.379+09:00  INFO 54057 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 42ms
2024-10-22T22:59:44.381+09:00  INFO 54057 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step2]
2024-10-22T22:59:44.382+09:00  INFO 54057 --- [           main] o.e.s.step.SimpleStepsJobConfig          : step2 item = 1
```

- 위 로그를 확인해보면 step1인 종료된 이후 step2가 실행된 것을 확인할 수 있으며, 둘다 main 스레드에서 실행되었음을 확인할 수 있다.
- 물론 simpleStepsJob의 처리 속도가 느리지는 않겠지만, 해당 Job의 처리 속도를 올려한다는 니즈가 발생하여 이를 해결하기 위한 업무를 진행한다고 가정해보자.

### Multi-threaded Step

### Parallel Steps

- Parallel Steps은 하나의 Job에 여러 Step을 병렬로 실행켜준다.

> > [Spring Batch > Scaling and Parallel Processing](https://docs.spring.io/spring-batch/reference/scalability.html)