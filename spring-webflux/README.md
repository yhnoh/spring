### Spring WebFlux

- Spring WebFlux는 작은 하드웨어 리소스와 적은 수의 스레드로 동시성을 처리하는 웹 프레임워크이다.
  - async, non-blocking I/O로 구축된 서비(Netty)를 이용한다.
- 함수형 프로그래밍을 지향하며, (CompleteableFuture와 ReactiveX에 의해 대중화된) 비동기 스트림을 이용하여 구현된다.
- Reactive는 이벤트에 따라서 반응한는 프로그래밍 모델을 말한다.
  - I/O 이벤트 (요청? 응답 이벤트?)
  - 마우스나 키보드와 같은 이벤트를 통해서 UI 컨트롤러의 변화
- syncronize는 사용자를 강제적으로 대기시키게끔 하지만,
- Reactive Stream은 구독자에게 얼마나 빠르게 또는 느리게 데이터를 생성하는지 제어하도록 하는 것이 목적이다.


`RouterFunction`


> https://docs.spring.io/spring-framework/reference/web/webflux/new-framework.html