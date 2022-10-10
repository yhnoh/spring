### 스프링 부트의 다중 요청 처리
---

- 스프링 부트는 하나의 프로세스에서 돌아가는 멀티 스레드 구조를 가지고 있다.
- 멀티 스레드 구조이기 때문에 사용자 한명의 요청이 아닌 여러 사용자의 요청을 처리할 수 있다.
- 실제로는 스프링 부트가 다중요청을 처리하는 것이 아닌, 스프링 부트에 내장되어있는 서블릿 컨테이너(tomcat)에서 다중 요청 처리해준다.
- 다중 요청 처리를 위해서 멀티 스레드 구조로 되어 있기 때문에 어떻게 스레드를 관리하는 알 필요성이 있다.


### 스레드 풀
---

- Thread Pool은 프로그램이 실행할 때 필요한 Thread를 미리 생성해 놓고 관리한다.
- 만약 Thread를 미리 생성하지 않으며 관리하지 않고 요청이 들어올 때 마다 하나씩 생성하면 어떤 문제가 있을까?
    1. 스레드 생성 비용이 생각보다 높기 때문에 많은 부담을 준다.
    2. 일단 요청이 들어온 순간부터 스레드를 무조건 생성을 해야하기 때문에 많은 동시 요청의 경우 스레드 생성의 억제가 어려워 서버가 다운될 확률이 눞다.
        - 실제로 Tomcat 3.2 이전 버전에는 유저의 요청마다 Thread를 생성하였다.

#### 스레드 풀 구조

![](./img/thread-pool-structure.png)

#### 스레드 풀에서의 작업 처리 과정
1. 첫 요청이 들어올 경우 core-size 만큼 스레드가 생성된다.
2. 유저 요청이 들어올 때마다 작업 큐에 담아둔다.
3. core-size의 스레드 중, 유휴상태(idle) 상태인 스레드가 있다면 작업 큐에서 작업을 꺼내 스레드에 작업을 할당하여 처리한다.
    > 1. 만약 유휴상태인 스레드가 없다면, 작업은 작업 큐에서 대기한다.
    > 2. 그 상태가 지속되어 작업 큐가 꽉 찬다면, 스레드를 새로 생성한다.
    > 3. 위 과정을 반복하다가 스레드 최대 사이즈에 도달하고 작업큐도 꽉 차게되면, 추가 요청에 대해서는 connection-refused 오류를 반환한다.
4. 작업이 완료되면 스레드는 다시 유휴 상태로 된다.
    1. 작업큐가 비어있고 core-size이상의 스레드가 생성되었다면 스레드를 제거한다.

> 스레드가 너무 많으면 너무 많은 스레드가 cpu의 자원을 두고 경합하게 되므로 처리속도가 느려질 수 있기 때문에 적절한 수로 유지되는 것이 가장 좋다. <br/>
> 키워드 : `스레드풀 전략`,`적정 스레드 개수`

#### 스레드 풀 관련 Property

```groovy
//application.yml
server:
  tomcat:
    threads:
      max: 200          // 생성할 수 있는 최대 스레드 수
      min-spare: 10     // 최소 유지되어야하는 스레드 수
    accept-count: 100   // 작업 큐 사이즈
```


### 스레드 풀 테스트
---

- 스레드 개수와 작업 큐의 개수를 최소한으로 잡아서 실제로 유저요청이 거절되는지 확인해보자.

#### application.yml에서 스레드풀 설정

```groovy
server:
  tomcat:
    threads:
      max: 2
      min-spare: 1
    accept-count: 1
``` 
- 최소 2개의 스레드가 생성가능하며, 1개의 요청은 작업 큐에서 대기한다.
- 총 3개의 요청 처리가 가능하다.

#### 3초간 대기 이후 응답
```java
@RestController
@Slf4j
public class HelloController {

    @GetMapping
    public String hello() throws InterruptedException {
        log.info("start");
        Thread.sleep(3000);
        log.info("end");
        return "hello world";
    }
}
```

#### 테스트 코드를 활용한 요청
```java
@Slf4j
public class SpringThreadPoolTest {

    @Test
    public void tomcatThreadTest() throws InterruptedException {


        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {

                String url = "http://localhost:8080";
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            });
            thread.start();
        }

        Thread.sleep(10000);
    }
}
```
- 최대 2개의 스레드와 1개의 작업큐에서 작업을 대기시킬 수 있으니 총 3개의 요청 처리는 가능하지만 나머지 요청은 거절되어야 한다.

#### 테스트 결과

![](./img/thread-test-result.png)
- 결과를 확인하면 모든 요청을 거절하지 않고 모두 처리되었다.
    -  물론 가끔씩 클라이언트 쪽에서 요청 에러가 발생하기도 한다.
        > `org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8080": Error writing to server; nested exception is java.io.IOException: Error writing to server` 
    - 하지만 무조건 스레드 최대 개수 + 작업 대기 개수를 초과한다고 해서 클라이언트의 요청을 거절하지 않는다.
    어떻게 된 것일까?

### BIO Connector와 NIO Connector
---

- 현재까지 가정한 이야기는 BIO (Blocking I/O) Connector일 때 유효한 이야기다.
- 톰캣 8.0부터 NIO(NonBlocking I/O) Connector이 기본적으로 채택되어 있다.
- 톰캣 9.0부터는 BIO Connector는 deprecate 되었다.

### Connector

### BIO Connector

### NIO Connector

### Spring 비동기 처리



> **Reference**
> - [spring-application-properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
> - [스프링부트는 어떻게 다중 유저 요청을 처리할까?](https://velog.io/@sihyung92/how-does-springboot-handle-multiple-requests)
> - [Core pool size vs maximum pool size in ThreadPoolExecutor](https://stackoverflow.com/questions/17659510/core-pool-size-vs-maximum-pool-size-in-threadpoolexecutor)
> - [Spring Boot Embeded Tomcat 내장 톰캣 application.properties 설정](https://aljjabaegi.tistory.com/601)
> - [@Async에서 사용하는 ThreadPoolTaskExecutor 최적화하기](https://sabarada.tistory.com/215)
> - [ThreadPoolTaskExecutor를 이용하여 성능 개선하기](https://kim-jong-hyun.tistory.com/104)
> - [ThreadPoolTaskExecutor의 RejectedExecutionHandler 설정](https://jessyt.tistory.com/171)