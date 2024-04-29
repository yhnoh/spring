### Spring WebFlux

- Spring WebFlux는 async, non-blocking I/O을 이용한 요청과 응답을 통해서 적은 하드웨어 리소스로 높은 처리량을 처리할 수 있는 애플리케이션이다.
  - async, non-blocking I/O로 구축된 서비(Netty)를 이용한다.
- aync, non-blocking 스트림 API(CompleteableFuture, ReactiveX)로 구성된 프로그래밍을 지원하며, 함수형(Funtional) 엔드포인트도 지원을 한다.
  - 기존 Spring MVC에서 사용하는 @Controller와 같은 어노테이션 기반의 엔드포인트도 지원한다.



### Spring MVC vs Spring WebFlux 요청 과정 




> https://howtodoinjava.com/spring-webflux/spring-webflux-tutorial/

- Reactive는 이벤트에 따라서 반응하는 프로그래밍 모델을 말한다.
  - I/O 이벤트 (요청? 응답 이벤트?)
  - 마우스나 키보드와 같은 이벤트를 통해서 UI 컨트롤러의 변화
- syncronize 웹 서버의 경우에는 사용자의 요청으로 인해서 응답이 완료되기 까지 
- Reactive Stream은 구독자에게 얼마나 빠르게 또는 느리게 데이터를 생성하는지 제어하도록 하는 것이 목적이다.

> https://www.baeldung.com/spring-webflux


> Spring Webflux를 통해서 API를 제작할 때, 중간에 blocking될 수 있는 메소드를 사용하지 않는 것이 좋다. 그렇지 않으면 Spring Webflux를 사용하는 이점이 사라진다.

> Do Not Block!
We must make sure that we don’t use any blocking methods throughout the lifecycle of an API. Otherwise, we lose the main advantage of reactive programming!

### Spring MVC vs Spring WebFlux

![](./img/spring-mvc-vs-webflux.png)

- Spring MVC에서는 blocking 서버를 사용하기 때문에 사이즈가 큰 스레드풀을 생성하여 사용하기 때문에 많은 리소스가 필요하다.
- Spring WebFlux는 non-blokcing 서버를 사용하기 때문에 사이즈가 작은 스레드풀로도 많은 사용자의 요청을 처리할 수 있다.


### Reactive Programming

- 전통적인 스트림은 blocking, sequential, fifo(first-in-first-out) 구조를 가지고 있다.
- 

Reactive programming is a programming paradigm that helps to implement non-blocking, asynchronous, and event-driven or message-driven data processing.

### Functional Routing and Handling

- Spring Webflux는 함수를 이용하여 요청에 대한 응답을 핸들링할 수 있다.
- 기본적으로 Spring MVC에서는 @Controller라는 어노테이션 기반으로 제작을 한다.
- 

> https://reflectoring.io/getting-started-with-spring-webflux/
`RouterFunction`


> https://docs.spring.io/spring-framework/reference/web/webflux/new-framework.html