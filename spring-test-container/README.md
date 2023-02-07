
#### Mac Mariadb Error 
- https://github.com/testcontainers/testcontainers-java/issues/5781 

#### TestContainer 사용하는 이유
- 테스트 환경을 만드는 과정에서 신경써야 할 부분이 바로 멱등성
  - 동일 input, 동일 output
- 외부 모듈로 인해서 테스트가 간헐적으로 실패할 수 있음
  - 이로 인해서 지속적인 관리가 들어가야하며, 해당 테스트 코드를 신뢰하지 못하는 현상 발생


#### 멱등성(idempotent)

- 연산을 여러 번 적용해도 결과가 바꾸지 않는 성질
  - 여러 번 함수를 실행하더라도 같은 결과가 나와야한다.
- 테스트에세는 매우 중요한 개념이며 멱등성을 지키는 것이 테스트 전체의 생산성에 아주 큰 영향을 줄 수 있다.


#### 사용하고자 하는 DB의 Embedded Library 사용
- https://github.com/wix/wix-embedded-mysql
- 특정 DB에 종속적인 기능을 테스트하기 어려울 경우 해결할 수 있는 방안
- 다만 DB별로 Embedded Library가 존재하지 않을 수 있음
- 