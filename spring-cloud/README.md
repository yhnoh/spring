MSA 느슨한 결합이 핵심


- EDA 기반의 접근 방식으로 특정 라이브러리나 서비스에 밀접하게 결합하지 않고, 변화에 대응할 수 있는 분리된 시스템을 구축할 수 있다.
  - 느스한 결합
- 캐싱된 데이터는 어디에서 읽어오든 동일한 읽기가 보장되어야 한다.
  - 즉, 로컬 내에서 데이터를 캐싱할 수 없다.
  - 로컬 캐시는 해당 서비스의 메모리를 증가시킬 수 있고, 동기화가 어렵다는 단점이 있다.
  - 데이터의 변화가 있었을 때, 캐싱 데이터의 변화가 있어야 한다.
- 데이터의 변화가 있었을 때, 변경된 내용을 알리려고 비동기 이벤트를 발송할 수 있다.
  - 느슨한 결합
  - 내구성
    - 
  - 확장성
  - 유연성
- 이벤트 기반 아키테처는 복잡해질 수 있는 단점이 있다.
- 이벤트 기반 아키테거는 선형적인 방식으로 처리되지 않기 때문에, 비지니스 로직을 추론하는 것이 어렵다.
- 메시지 처리의 의미론
- 데이터의 변화가 있을 때, REST API를 활용하여 요청-응답 모델로 구성했을 때, 유지보수 관점에서 쉽지 않다.
  - 서비스끼라 강한 결합이 발생할 수 있다.
  - 캐시를 변경하는 엔드포인트가 변경되면, 관련되어 있는 서비스들도 전부 변경해야 한다. 
  - 서비스간 강한 결합도가 발생
  - 서비스의 변경이 다른 서비스에 영향을 줄 수 있다. 


https://medium.com/@odysseymoon/spring-cloud-stream-with-rabbitmq-c273ed9a79b

https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#spel-and-streaming-data

https://www.springboot.kr/posts/spring/spring-cloud-stream-binder-kafka/