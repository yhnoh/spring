### 1. Spring Batch Exit Code로 인한 문제 발생
---

- 실무에서 젠킨스와 cli를 통해서 Spring Batch Job을 실행시켜주고 있는데 Spring Batch Job이 실패를 하여도 젠킨스에서는 성공되었다는 메시지가 나오는 문제점이 있었다.
- 이렇게 될경우 젠킨스 파이프라인에서 Spring Batch Job이 실패하여도 젠킨스 파이프라인 스텝에서는 성공했다고 인식하기 때문에 다음 스텝을 계속해서 실행시켜주게 된다.
  - 만약 해당 내용을 수정하지 못하게 된다면, 하나의 Spring Batch Job 내에 모든 로직을 담을 수 밖에 없는 문제가 발생하기 때문에 해당 문제를 해결해야 했다.

### 2. 원인 파악 및 어떻게 해결할까?
---

- 해당 이슈가 발생한 이유는 Spring Batch Job이 실패하여도 exit code가 0을 리턴하게 된다는 사실을 알게되었다.
```text
//...
2023-10-07 16:49:56.235  INFO 12305 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=failedJob]] launched with the following parameters: [{}]
2023-10-07 16:49:56.259  INFO 12305 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [failedStep]
2023-10-07 16:49:56.270 ERROR 12305 --- [           main] o.s.batch.core.step.AbstractStep         : Encountered an error executing step failedStep in job failedJob

java.lang.RuntimeException: 실행 도중 에러 발생
//....

2023-10-07 16:49:56.274  INFO 12305 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [failedStep] executed in 14ms
2023-10-07 16:49:56.279  INFO 12305 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=failedJob]] completed with the following parameters: [{}] and the following status: [FAILED] in 31ms
2023-10-07 16:49:56.286  INFO 12305 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2023-10-07 16:49:56.292  INFO 12305 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

Process finished with exit code 0
```
- Spring Batch Job이 실패 했을 때 해당 exit code가 0이 아닌 다른 값으로 리턴되도록 설정해야 젠킨스에서도 실패했다는 여부를 알 수 있게 된다.
- 이를 해결하기 위해서는 Spring Batch Job이 실패했을 때를 catch하여 exit code를 사용자 정의화 해야한다.

#### 2.1. 스프링 어플리케이션 Exit Code 사용자 정의화 하기
- 스프링에서는 `ExitCodeGenerator` 인터페이스를 구현하여 Bean에 등록하면 사용자 정의 종료 코드를 반환할 수 있다.
    ```java
    @SpringBootApplication
    public class MyApplication {
    
        @Bean
        public ExitCodeGenerator exitCodeGenerator() {
            return () -> 42;
        }
    
        public static void main(String[] args) {
            System.exit(SpringApplication.exit(SpringApplication.run(MyApplication.class, args)));
        }
    
    }
    ```
    - SpringApplication.exit가 호출되면 사용자가 정의한 종료코드를 반환하게 되며, 반환된 종료 코드를 System.exit()에 전달하여 어플리케이션을 종료시킬 수 있다.
    > https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.spring-application.application-exit

### 3. 구현하기

- 현재 내가 원하는 것은 Spring Batch Job이 성공 또는 실패했는지를 알아내어, 종료 코드를 반환하는 것이다.
- `JobLauncherApplicationRunner`코드를 보면 잡 실행 이후 해당 상태를 `ApplicationEventPublisher`를 통해서 이벤트를 발행하고 있다는 것을 알 수 있다.
  ```java
  public class JobLauncherApplicationRunner implements ApplicationRunner, InitializingBean, Ordered, ApplicationEventPublisherAware {
                    
      //...
      private ApplicationEventPublisher publisher;
    
      protected void execute(Job job, JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobParametersNotFoundException {
          JobParameters parameters = getNextJobParameters(job, jobParameters);
          JobExecution execution = this.jobLauncher.run(job, parameters);
          //ApplicationEventPublisher를 통해서 Event 발행
          if (this.publisher != null) {
              this.publisher.publishEvent(new JobExecutionEvent(execution));
          }
      }
      //...
  }
  ```
  - `JobLauncherApplicationRunner`는 jobName과 jobParameter를 받아서 Job을 실행해주는 클래스이다.
  - `execute`메서드는 Job을 실행시켜주는 메서드이며 Job이 실행 완료된 이후에 `ApplicationEventPublisher`를 통해서 `JobExecution`객체를 발행하는 것을 확인할 수 있다.
- `ApplicationListener`를 인터페이스를 구현하여 `JobExecution` 객체를 받아와 `BatchStatus`를 통해서 종료 코드를 커스터마이징하면 간단하게 문제를 해결할 수 있다.
    ```java
    @SpringBootApplication
    @EnableBatchProcessing
    @RequiredArgsConstructor
    public class SpringBatchExitCodeApplication {

        public static void main(String[] args) {
            System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchExitCodeApplication.class, args)));
        }

        @Bean
        public ExitCodeGenerator exitCodeGenerator(){
            return new JobExitCodeGenerator();
        }

        //ApplicationListener를 통해서 JobExecution객체를 전달받아, Job의 성공 또는 실패 여부를 종료 코드에 전달한다.
        class JobExitCodeGenerator implements ExitCodeGenerator, ApplicationListener<JobExecutionEvent> {
            private JobExecution jobExecution;

            @Override
            public int getExitCode() {
                //성공은 0, 그 이이의 상태는 1 이상이다.
                return jobExecution.getStatus().ordinal();
            }

            @Override
            public void onApplicationEvent(JobExecutionEvent jobExecutionEvent) {
                this.jobExecution = jobExecutionEvent.getJobExecution();
            }
        }
    }
    ```
- 사실 직접 정의하지 않아도 스프링 배치 어플리케이션에서 이미 구현이 되어 있기 때문에 따로 정의하지 않아도 되다.
    ```java
    public class BatchAutoConfiguration {
        //...
        @Bean
        @ConditionalOnMissingBean(ExitCodeGenerator.class)
        public JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator() {
            return new JobExecutionExitCodeGenerator();
        }
        //...
    }
    ```
    - `@ConditionalOnMissingBean`은 해당 클래스로 구현된 빈이 없으면, 현재 등록된 빈이 사용되도록 하는 어노테이션이다.
- `JobExecutionExitCodeGenerator`으로 Spring Batch Job이 성공이 아닐 경우 다른 exit code를 리턴하기 때문에 따로 정의하지 않아도 된다.
    ```java
    @SpringBootApplication
    @EnableBatchProcessing
    @RequiredArgsConstructor
    public class SpringBatchExitCodeApplication {

        public static void main(String[] args) {
            System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchExitCodeApplication.class, args)));
        }
    }
    ```
    - `SpringApplication.exit`내에서 `ExitCodeGenerator`빈을 가져오는 코드를 확인할 수 있다.