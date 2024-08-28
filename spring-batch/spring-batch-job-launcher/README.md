### Spring Batch Multiple Job
- 스프링 배치 2.X 버전대에서는 `spring.batch.job.name`에서는, 쉼표로 구분하여 Multiple Job을 실행시킬 수 있다.
    > java -jar --job.names=helloJob2,helloJob1
- 하지만 위와 같은 방식으로 여러 잡을 실행시켰을 때 위 순서대로 잡이 실행하기를 예상했지만, 실제로는 그렇지 않은 문제가 발생하였다.
- 
- 위의 문제를 해결하기 위하여 `JobLauncherApplicationRunner`를 재가공하였다
- 왜 해당 클래스를 재가공할 수 밖에 , 그리고 왜 `JobLauncherApplicationRunner` 클래스를 재가공하여 사용할 수 밖에 없었는지에 대하여 한번 살펴보자.

> 여담이지만 스프링 배치 3.X 버전부터는 Multiple Job을 지원하지 않는다.
> [스프링 배치 3.0 마이그레이션 가이드](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
    > Running multiple batch jobs is no longer supported.
    > If the auto-configuration detects a single job is, it will be executed on startup.
    > If multiple jobs are found in the context, a job name to execute on startup must be supplied by the user using the spring.batch.job.name property.

### Spring Batch Multiple Job
- 

### Spring Batch Multiple Job 순서대로 실행

#### Job Setting

```java
@Configuration
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job helloJob1() {
        return jobBuilderFactory.get("helloJob1").start(this.helloStep1()).build();
    }


    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1").tasklet((contribution, chunkContext) -> {
            System.out.println("helloJob1 run!!!");
            return RepeatStatus.FINISHED;
        }).build();
    }


    @Bean
    public Job helloJob2() {
        return jobBuilderFactory.get("helloJob2").start(this.helloStep2()).build();
    }


    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2").tasklet((contribution, chunkContext) -> {
            System.out.println("helloJob2 run!!!");
            return RepeatStatus.FINISHED;
        }).build();
    }

}
```

- `HelloJobConfig` 클래스에 `helloJob1`과 `helloJob2` 잡을 셋팅해두었다.
- jar 파일을 실행시킬 때, `--job.names` 프로퍼티에 `helloJob2,helloJob1` 값을 넣고 실행시켰을 때, `helloJob2`가 먼저 실행되고 `helloJob1`이 실행되는 것을 기대하였지만, `helloJob1`이 먼저 실행된 이후 `helloJob2`가 실행되는 것을 확인할 수 있다.
    ```text
    java -jar ./build/libs/spring-batch-job-launcher-0.0.1-SNAPSHOT.jar --job.names=helloJob2,helloJob1

    ....
    2024-08-02 13:53:22.487  INFO 39927 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=helloJob1]] launched with the following parameters: [{}]
    2024-08-02 13:53:22.522  INFO 39927 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [helloStep1]
    helloJob1 run!!!
    2024-08-02 13:53:22.534  INFO 39927 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [helloStep1] executed in 11ms
    2024-08-02 13:53:22.536  INFO 39927 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=helloJob1]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 36ms
    2024-08-02 13:53:22.543  INFO 39927 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=helloJob2]] launched with the following parameters: [{}]
    2024-08-02 13:53:22.551  INFO 39927 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [helloStep2]
    helloJob2 run!!!
    2024-08-02 13:53:22.556  INFO 39927 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [helloStep2] executed in 5ms
    2024-08-02 13:53:22.561  INFO 39927 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=helloJob2]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 16ms
    ....
    ```


- 하지만 기대한 결과와는 다르게 `helloJob1`이 먼저 실행된 이후 `helloJob2`가 실해

- [HelloJobConfig.class](./src/main/java/org/example/springbatchjoblauncher/v1/HelloJobConfig.java)
- 해당 HelloJob을 실행시켜
org.example.springbatchjoblauncher.v1