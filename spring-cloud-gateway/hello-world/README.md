
### Spring Cloud Gateway의 필요성
- MSA와 같은 분산형 아키텍처는 각 서비스별로 필요한 비지니스 로직들을 수행하게 되지만, 개발이 진행될 수록 모든 서비스에 공통적으로 적용되어야할 기능들이 존재하게된다. 
- 이러한 기능은 인증, 권한, 로깅, 모니터링, 트래픽 제어, 라우팅 등이 있다.
- 클라이언트가 각 서비스의 엔드포인트를 직접 호출 하게되며 단일 지점이 없음

- 공통 로직을 각 서비스들이 구현하게 될 경우 아래와 같은 문제 발생
- 모든 서비스가 일관성 있는 개발의 어려움
- 모든 서비스가 공통 로직을 구현하게 될 경우 변경의 어려움
- 클라이언트가 각 서비스의 엔드포인트를 직접 호출하는 것이 아닌, 게이트웨이를 통해 호출하게 되면 이러한 공통 기능들을 게이트웨이에서 처리할 수 있다.
- 게이트웨이는 클라이언트의 요청을 받아서 필요한 처리를 한 후, 각 서비스로 요청을 전달하고 응답을 클라이언트에게 전달하는 역할을 한다.

- Route Predicate Factories
- GatewayFilter Factories


- [Spring Cloud Gateway](#spring-cloud-gateway)
- 
  - 
  - [Spring Cloud Gateway란?](#spring-cloud-gateway란)
  - [Spring Cloud Gateway 특징](#spring-cloud-gateway-특징)
  - [Spring Cloud Gateway 구성요소](#spring-cloud-gateway-구성요소)


- Predicate
- Filter
  - GatewayFilter: HTTP 요청과 응답을 수정할 수 있는 필터

> https://toss.tech/article/slash23-server
> https://www.igloo.co.kr/security-information/spring-security-part1-%ec%9d%b8%ec%a6%9d%ea%b3%bc-%ec%9d%b8%ea%b0%80-%ec%95%84%ed%82%a4%ed%85%8d%ec%b2%98%ec%9d%98-%ec%9d%b4%ed%95%b4%ec%99%80-%ec%8b%a4%ec%a0%84-%ed%99%9c%ec%9a%a9/
> https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories/rewritepath-factory.html
> 스프링 마이크로서비스 코딩 공작 개정 2판 8장 > 스프링 클라우드 게이트웨이를 이용한 서비스 라우팅
> 