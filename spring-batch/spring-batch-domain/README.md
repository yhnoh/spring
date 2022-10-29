### Spring Batch Domain
---

![](./img/spring-batch-domain.png)

- Job 과 Step은 일대다 관계를 가지면 하나의 Job은 여러 Step을 실행시킬 수 있다.
- 각 Step은 하나의 ItemReader, 하나의 ItemProcessor 및 하나의 ItemWriter가 있다.
- Job을 실행 시키기 위해서는 JobLanucher가 필요하다.
- JobRepository는 JobLauncher를 통해 Job을 실행하고, Job은 각 Step을 실행하는 과정의 메타데이터 정보를 가지고 있다.

### 간단한 HelloJob 만들기
---

#### HelloJob class
```java
@Configuration
@RequiredArgsConstructor
@Slf4j
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(startStep())
                .next(nextStep1())
                .next(nextStep2())
                .build();

    }


    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("helloStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("firstStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step nextStep1() {
        return stepBuilderFactory.get("nextStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("nextStep1");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step nextStep2() {
        return stepBuilderFactory.get("nextStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("nextStep2");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
```
#### 결과

![](./img/hello-job.png)

- 먼저 JobLauncher가 helloJob을 실행시킨다.
- job이 실행되면서 실제 작업이 진행될 각 step이 실행된다.
- JobLauncher가 실행되면서 내가 어떤 파라미터 값을 전달할 수 있다는 것을 확인할 수 있다.
    - `o.s.b.c.l.support.SimpleJobLauncher: Job: [SimpleJob: [name=helloJob]] launched with the following parameters: [{}]`


### Job
---

- Job은 Step을 모아놓은 하나의 Container다.
- Job의 구성
  - Job의 이름을 지정한다.
  - Step의 실행 순서를 정의한다.
  - Job을 다시 실행시킬지 말지를 정의한다.
- Job 인터페이스는 SimpleJob을 통해서 기본적으로 구현되어 있다.

```java
@Bean
public Job helloJob() {
    return jobBuilderFactory.get("helloJob")
            .start(startStep())
            .next(nextStep1())
            .next(nextStep2())
            .build();

}
```

#### Job Hierarchy
![](./img/job-hierarchy.png)

#### JobInstance

- JobInstance는 ***Job과 JobParmeters가 전달되어 만들어지는 실행가능한 논리적 작업 단위다.***
  - `JobInstance = Job + identifying JobParameters.`
- JobInstance에는 실제 내부 동작을 위한 필드나 메소드가 존재하지 않고, Job의 이름과 JobInstance를 구분하기 위한 Id만 존재한다.
- 새로운 JobInstance를 사용한다는 것은 '처음부터 시작'을 의미하고, 기존 JobInstance를 사용하는 것은 일반적으로 '중단한 곳에서 시작'을 의미
  - 생성 및 기존 JobInstance를 결정 짓는 여부는 JobParameters이다.

#### JobParameters

- JobParameter란 ***job을 실행할 때 전달되는 값***이다.
- JobParameter에는 파라미터로 전달될 키와 값이 존재하고 전달 가능한 타입은 STRING, DATE, LONG, DOUBLE 4개가 있다.
  
#### JobExecution

- JobExecution은 Job과 JobParameters로 만들어진 JobInstance의 실제 실행을 나타내는 객체이다.
- 어떤 JobParametes가 전달되었고 작업 기준이 되는 JobInstance가 무엇인지를 가지고 잇다.
- 작업 실행중 유지/공유되는 정보인 ExecutionContext를 가지고 있다.
- 작업 실해에 관련된 다양한 정보를 가지고 있다.
  - 실행중/종료시 상태, 실패시 발생한 에러, 생성/시작/종로/마지막수정 시간 정보
- 작업이 실패된다면 동일 JobInstance로 새로운 JobExecution이 생길 수 있다.



#### Reference
> - [Spring Batch Domain](https://docs.spring.io/spring-batch/docs/4.2.x/reference/html/domain.html#item-processor)
> - [스프링 배치: Job, JobParameter, JobInstance, JobExecution](https://nankisu.tistory.com/69)