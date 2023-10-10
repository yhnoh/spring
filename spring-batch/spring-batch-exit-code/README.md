- Spring Batch Exit Code

- Spring Batch Job 실행하는 도중 에러가 발생 했을 때 exit code가 0을 리턴하게 된다.
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
- exit code 0을 리턴하기 때문에 cli를 이용하여 Spring Batch Job을 실행했을 경우 Job의 성공이나 실패 여부를 확인할 수 없다.
- 이를 해결하기 위해서는 Spring Batch Job이 실패했을 때를 catch하여 exit code를 사용자 정의화 해야한다.

1. SpringApplicaion이 종료될 때, 사용자 정의 종료 코드 반환하기
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
- `ExitCodeGenerator`인터페이스를 구현하고 Bean에 등록하여 사용자 정의 종료 코드를 반활할 수 있다.
- SpringApplication.exit가 호출되면 사용자가 정의한 종료코드를 반환하게 되며, 반환된 종료 코드를 System.exit()에 전달하여 어플리케이션을 종료시킬 수 있다. 

> https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.spring-application.application-exit

1. SpringBatch Job이 실행이 완료된 이후, 이벤트 받기
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
``

- 
- 때문에 외부 스케쥴러를 활용하여 Spring Batch Job을 실행하게 될경우 성공 처리가 되어 해당 Job의 실패 여부를 확인하기 위해서는 직접 확인해야한다.
- 이 문제를 해결하기 위해서는 Spring Batch Job이 실패했을 때, exit code를 0이 아닌 다른 값을 리턴하면 해결할 수 있다.


- ExitCodeGenerator 인터페이스를 구현하여 사용자 정의 exit code 리턴
- Job이 성공 또는 실패르를 확인할 수 있는 EventListener 확인


- job이 실패해도 exit code는 정상



https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.spring-application.application-exit


https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.spring-application.application-exit