### Spring Cloud Gateway의 필요성

- MSA와 같은 분산형 아키텍처는 각 서비스별로 필요한 비지니스 로직들을 수행하게 되지만, 개발이 진행될 수록 모든 서비스에 공통적으로 적용되어야할 기능들이 존재하게된다.
- 이러한 기능은 인증, 권한, 로깅, 모니터링, 트래픽 제어, 라우팅 등이 있다.
- 클라이언트가 각 서비스의 엔드포인트를 직접 호출 하게되며 단일 지점이 없음

- 모든 서비스 경로를 단일 URL로 매핑
    - MSA환경에서 많은 서비스 호출 지점을 단일 URL로 매핑하여 클라이언트는 해당 URL로만 호출하면된다.
- 공통 기능을 처리할 수 있는 필터 제공
- 요청을 서비스로 라우팅하기 전 해당 요청이 조건을 충족하는지 확인

- 공통 로직을 각 서비스들이 구현하게 될 경우 아래와 같은 문제 발생
- 모든 서비스가 일관성 있는 개발의 어려움
- 모든 서비스가 공통 로직을 구현하게 될 경우 변경의 어려움
- 클라이언트가 각 서비스의 엔드포인트를 직접 호출하는 것이 아닌, 게이트웨이를 통해 호출하게 되면 이러한 공통 기능들을 게이트웨이에서 처리할 수 있다.
- 게이트웨이는 클라이언트의 요청을 받아서 필요한 처리를 한 후, 각 서비스로 요청을 전달하고 응답을 클라이언트에게 전달하는 역할을 한다.

- Route Predicate Factories
    - Spring Gateway는 요청을 처리하기 전에, 해당 요청이 특정 조건들을 만족하는지 확인한다.
    - 해당 요청이 특정 조건을 만족하게 되면, 해당 요청을 처리할 서비스로 라우팅한다.
- GatewayFilter Factories
    - Spring Gateway는 서비스에 호출하기 전/후에 필요한 기능을 수행할 수 있는 GatewayFilter를 제공한다.
    - GatewayFilter를 통하여 요청/응답을 수정하거나, 공통 기능을 수행할 수 있다.
        - ex) 인증, 권한, 로깅, 모니터링, 트래픽 제어 등...

### API 게이트웨이 패턴

- API 게이트웨이는 클라이언트가 서버에 요청을 하는 단일 창구 역할을 한다.
    - MSA 환경에서 많은 서비스 호출 지점을 단일 URL로 매핑하여 클라이언트는 해당 URL만 호출하면된다.
- 인증이나 모니터링과 같은 공통 기능에 대한 담당도 ㅈㄴ행한다.
- 엣지 기능 구현
    - 인증, 인가, 사용량 제한, 캐싱, 지표수집, 요청 로깅

- 장점
    - 내부 구조를 캡슐화를 하면 단일 창구 구조를 가질 수 있다. 이로인해서 클라이언트 입장에서는 단일 URL로 서비스 호출이 가능하다.
    - 엣지 기능을 구현하며, MSA 환경에서 모든 서버가 동일한 기능을 구현할 필요 없이 API 게이트웨이에서 처리할 수 있다.
- 단점
    - 개발, 배포, 관리 해야하는 고가용 컴포넌트가 하나 더 늘어난다.
    - 개발 프로세스가 늘어나며, 게이트웨이를 업데이트 하지않으면 새로운 API를 사용할 수 없다.
    - 단일 장애 지점이 발생할 . 잇다.

- API 게이트웨이 설계 이슈
- 성능과 확장성
- 리액트비 프로그래밍 추상체를 이용한 관리 가능한 코드 작성
- 부분 실패 처리
- 애플리케이션 아키텍처에서 선량한 시민되기


- [Spring Cloud Gateway](#spring-cloud-gateway)
-
    -
    - [Spring Cloud Gateway란?](#spring-cloud-gateway란)
    - [Spring Cloud Gateway 특징](#spring-cloud-gateway-특징)
    - [Spring Cloud Gateway 구성요소](#spring-cloud-gateway-구성요소)

> 마이크로서비스, 8장 외부 API 패턴
> https://github.com/gilbutITbook/007035/tree/master/ftgo-api-gateway
> https://devocean.sk.com/blog/techBoardDetail.do?ID=165131

- Predicate
- Filter
    - GatewayFilter: HTTP 요청과 응답을 수정할 수 있는 필터

> https://toss.tech/article/slash23-server
> https://www.igloo.co.kr/security-information/spring-security-part1-%ec%9d%b8%ec%a6%9d%ea%b3%bc-%ec%9d%b8%ea%b0%80-%ec%95%84%ed%82%a4%ed%85%8d%ec%b2%98%ec%9d%98-%ec%9d%b4%ed%95%b4%ec%99%80-%ec%8b%a4%ec%a0%84-%ed%99%9c%ec%9a%a9/
> https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories/rewritepath-factory.html
> 스프링 마이크로서비스 코딩 공작 개정 2판 8장 > 스프링 클라우드 게이트웨이를 이용한 서비스 라우팅
> https://j-ungry.tistory.com/380 <br/>
> https://j-ungry.tistory.com/380 <br/>