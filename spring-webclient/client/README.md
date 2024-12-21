- 테스트 목록
    - 요청
    - 단일 응답 바디, 복수 응답 바디
    - 복수 응답 바디
    - Flux 블로킹 테스트
    - Mono 블로킹 테스트
    - 타임아웃 테스트
    - 예외 처리 템플릿 테스트
    - 필터

- `retrieve()` : 응답값을 어떻게 받을지 결정하기위한 함수이며, 체이닝을 통해서 실제 응답 값을 받아올 수 있다.
    - retrieve 함수 사용시 `toEntity(), bodyToMono(), bodyToFlux()`등을 통해서 응답 값을 받아 올 수 있다.
    - `toEntity()` 의 경우 ResponseEntity 객체로 받아올 수 있으며, `bodyToMono(), bodyToFlux()` 는 각각 Mono, Flux 객체로 받아올 수 있다.
    - 4xx, 5xx의 에러의 경우 기본적으로 `WebClientResponseException`이 발생을 하며 `onStatus`함수를 통해서 status 코드에 따라서 에러 핸들링이 가능해진다.


-

- error 핸들링
- onStatus : ResponseSpec
- onErrorResume
- onErrorReturn
- onErrorMap
- doOnError

- WebClientException
- WebClientResponseException
- WebClientRequestException

etrive() 를 사용할 때는, toEntity(), bodyToMono(), bodyToFlux() 이렇게 response를 받아올 수 있습니다.
bodyToFlux, bodyToMono 는 가져온 body를 각각 Reactor의 Flux와 Mono 객체로 바꿔줍니다.
이때, mono는 외부 서비스에 요청을 할 때 최대 하나의 결과를 예상할 때 Mono를 사용해야 합니다.
외부 서비스 호출에서 여러 결과가 예상되는 경우 Flux를 사용해야 합니다.
저희 서비스는 하나의 결과가 나오기 때문에, 응답 결과를 bodyToMono로 받아오겠습니다.
> https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html
> https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.Builder.html
> > https://gngsn.tistory.com/223
> https://gngsn.tistory.com/154
> https://sthwin.tistory.com/23