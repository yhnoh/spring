
### Spring Cache Abstraction을 사용하는 이유
- 서버는 클라이언트의 요청으로 인하여 저장소에 데이터 읽기/쓰기 작업을 한다. 각 서버별로 사용용도가 달라질 수는 있겠지만 서버는 주로 쓰기 작업보다 읽기 작업을 더 많이하게 된다.
- 클라이언트 <-> 서버 <-> 저장소간에 읽기 작업을 진행하면서 응답이 지연되면 이를 해결하기 위하여 캐시 도입을 고민하게 된다. 
- Spring에서는 `Spring Cache Abstraction`을 제공하며 이를 이용하여 캐시를 간단하게 구축할 수 있다.
  - AOP 및 어노테이션 기반으로 캐시 사용이 가능하기 때문에 코드 수정이 적음
  - `Cache` 및 `CacheManager`를 구현하여 다양한 캐시 저장소 사용 가능
  - 캐시 저장소를 변경 시, `Cache` 및 `CacheManager`만 변경면 되기 때문에 캐시가 적용중인 코드에 미치는 영향도가 적음

> [Spring > Understanding the Cache Abstraction](https://docs.spring.io/spring-framework/reference/integration/cache/strategies.html)

### 데이터 변경시 Cache를 삭제하며 발생한 이슈

- 기본적으로 캐시를 삭제하면  



### 


`CacheAspectSupport`

> > https://gompangs.tistory.com/entry/Spring-Redis-Template-Transaction
> https://velog.io/@daoh98/redisTemplate.hasKey-null