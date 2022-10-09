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
    2. 일단 요청이 들어온 순간부터 스레드를 무조건 생성을 해야하기 때문에 엄청나게 많은 동시 요청의 경우 스레드 생성의 억제가 불가능하며 서버가 다운될 확률이 눞다.
        - 실제로 Tomcat 3.2 이전 버전에는 유저의 요청마다 Thread를 생성하였다.

#### 스레드 풀 구조

![](./img/thread-pool-structure.png)






- [spring-application-properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [스프링부트는 어떻게 다중 유저 요청을 처리할까?](https://velog.io/@sihyung92/how-does-springboot-handle-multiple-requests)