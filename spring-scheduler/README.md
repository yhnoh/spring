### Spring Scheduler

---
- 개발자가 **일정 시간 간격으로 로직을 실행**시키고자 할 때 사용한다.
- Spring Boot Starter에 기본적으로 의존하고, 매우 간단하게 Scheduler를 만들 수 있으며 사용하는 방법도 간단하다.

### Spring Scheduler 사용법

---
- 스프링 실행 클래스에 `@EnableScheduling` 추가
  ```java
  @EnableScheduling
  @SpringBootApplication
  public class SpringSchedulerApplication {

      public static void main(String[] args) {
          SpringApplication.run(SpringSchedulerApplication.cla  ss, args);
      }
  }
  ```
- Scheduler를 사용할 클래스에 `@Component` 어노테이션 추가 및 메서드에 `@Scheduled` 어노테이션 추가
  ```java
  @Component
  public class SchedulerService {
      @Scheduled(fixedRate = 1000)
      public void ScheduledMethod(){
          //로직 ...
      }
  }
  ```
    - @Scheduled 규칙
      - 메서드는 void 타입
      - 메서드 매개변수 사용 불가

### @Scheduled 주요 필드

---

- fixedRate
	- 작성한 시간 간격으로 작업 시작
	- 이전 작업이 완료될 때까지 다음 작업이 진행되지 않음
- fixedDelay
	- 이전 작업이 종료된 후 설정시간 이후에 다시 시작
	- 이전 작업이 완료될 때까지 대기
- Cron
	- cron 표현식을 사용한 작업 예약

### Spring Scheduler Configuration
- Scheduler는 기본적으로 thread 1개를 이용하여 동기 형식으로 진행된다. 즉 이전 스케줄이 끝나지 않으면 다음 스케줄이 시작시간이 되었더라도 실행되지 않는다.

### Reference
> - [@Scheduled를 활용한 Spring Scheduler](https://velog.io/@kimh4nkyul/Scheduled%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-Spring-Scheduler)
> - [Spring Scheduler](https://data-make.tistory.com/699)