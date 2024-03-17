## 1. MSA 환경에서 이벤트 기반 아키텍처가 필요한 이유 (EAD: Event Driven Architecture)

- MSA 환경에서 애플리케이션 환경이 분리가 되어 있는데, 하나의 서비스에서 상태 변경이 일어났을때 다양한 서비스에도 영향을 끼쳐야하는 경우가 많다.
    - 예를 들어 주문 서비스에서 상품 정보를 캐싱하고 있는데, 상품 서비스에서 상품을 수정하게 되면 주문 서비스는 상품 캐시를 삭제하여 데이터의 일관성을 유지해야한다.
- 하나의 서비스내에서의 상태 변경이 여러 서비스에 걸쳐 영향을 끼쳐야 할때, 영향을 받아야하는 서비스에 엔드포인트를 요청하고 응답하는 형식으로 처리할 수도 있다.
    - 상품이 수정되면 상품 서비스는 주문 서비스에 요청을 하고 상품 캐시를 삭제한다.
- 위와 같은 방식이 처음에는 간단해보이지만, 서비스가 많이 분리가 되어가는 환경에서는 아래와 같은 문제가 발생할 수 있다.
    - 상태 변경을 하는 서비스가 어떤 서비스에 영향을 주어야하는지 전부 알고 있어야한다. 서비스간 직접 통신을 하기 때문에 해당 엔드포인트를 전부 알고 있어야한다.
    - 만약 영향을 주어야하는 서비스의 엔드포인트를 전부 알고 있더라도, 엔드포인트의 변경이 일어나는 시점에 관련된 서비스를 전부 수정하고 배포해야한다.
    - 서비스간 직접 통신을 통한 상태 변경은 담당하는 서비스 개발자 모두를 피곤하게 만들 수 있다.
- 서비스간 직접 통신을 통한 상태변경은 서비스간 강한 결합이 발생한다. 유지보수하기 쉬운 소프트웨어를 만들기위해서는 낮은 결합을 유지하는 것이 좋다.
- 이벤트 기반 아키텍처를 활용하면 강한 결합이 발생하는 서비스 관계를 느슨하게 결합하도록하여 MSA 환경에서 유지보수가 쉬운 소프트웨어를 만들어 나갈 수 있다.

## 2. MSA환경에서 이벤트 기반 아키텍처의 필요성을 단계적으로 알아보기

- 주문을 한 이후 이메일과 카카오톡 메시지를 보내는 코드를 작성해가며 MSA 환경에서 왜 이벤트 기반 아키텍처가 필요한지 단계적으로 알아가보자.

#### 2.1. 주문을 한 이후에 메시지 전송

```java

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ExceptionMessageService exceptionMessageService;

    public void orderSync(String name) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        exceptionMessageService.sendEmail(orderJpaEntity.getId());
        exceptionMessageService.sendkakaoTalk(orderJpaEntity.getId());
    }
}
```

- 주문을 한 이후에 `ExceptionMessageService`를 이용하여 이메일과 카카오톡 메시지를 전달한다.
- 일반적으로 위와 같이 코드를 작성하지만 메시지를 전송하는 서비스가 실패했을 경우 곧 주문의 실패로 이어진다는 큰 문제가 하나 있다.

#### 2.2. 주문을 한 이후에 메시지 전송 비동기 처리

```java

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final AsyncMessageService asyncMessageService;

    public void orderAsync(String name) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        asyncMessageService.sendEmail(orderJpaEntity.getId());
        asyncMessageService.sendkakaoTalk(orderJpaEntity.getId());
    }
}

@Service
@Transactional
@RequiredArgsConstructor
@Async
public class AsyncMessageService implements MessageService {

    private final ExceptionMessageService exceptionMessageService;

    @Override
    public void sendEmail(long orderId) {
        exceptionMessageService.sendEmail(orderId);
    }

    @Override
    public void sendkakaoTalk(long orderId) {
        exceptionMessageService.sendkakaoTalk(orderId);
    }
}
```

- 주문을 한 이후에 `AsyncMessageService`를 이용하여 이메일과 카카오톡 메시지를 전달한다.
- `AsyncMessageService`는 `@Async`어노테이션을 활용하여 비동기식으로 메시지를 전송하기 때문에 메시지 서비스의 실패가 주문의 실패로 이어지지 않는다.
- 하지만 `OrderService`에서 주문을 하는 코드와 메시지를 전송하는 코드의 위치를 변경하게 되었을 때, 주문은 실패했지만 메시지는 전송이 되는 문제가 발생할 수 있다.
- 더 나아가서 주문 서비스는 주문이 완료된 이후에 어떤 일을 해야하는지 알아야할 이유가 있을까라는 고민을 해볼 수 있다.
    - 만약 이런 고민이 없다면 주문 서비스를 개발하는 사람은 주문이 완료된 이후 어떤일이 일어나야하는지에 대한 모든 상황을 인지하고 있어야한다.
    - 또한 주문 완료 이후 처리되어야하는 코드가 변경되었을때 주문 서비스를 개발하는 개발자는 같이 수정을 해줘야한다.

#### 2.3. 주문 트랜잭션이 완료된 이후, 비동기적으로 메시지 전송하기

```java

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void orderTransactionalEventListener(String name) {

        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        OrderCompletedEvent orderCompletedEvent = new OrderCompletedEvent(this, orderJpaEntity.getId());
        applicationEventPublisher.publishEvent(orderCompletedEvent);
    }
}

@Component
@Async
@RequiredArgsConstructor
public class MessageTransactionalEventListener {

    private final DefaultMessageService defaultMessageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void completeOrder(OrderCompletedEvent orderCompletedEvent) {
        defaultMessageService.sendEmail(orderCompletedEvent.getOrderId());
        defaultMessageService.sendkakaoTalk(orderCompletedEvent.getOrderId());
    }
}
```

- 주문이 완료된 이후에 주문 성공 이벤트를 `ApplicationEventPublisher`를 이용하여 전달하고, 이메일과 카카오톡 메시지를 전달한다.
- `MessageTransactionalEventListener`는 `@TransactionalEventListener`어노테이션을 활용하여 메시지를 전송하기 때문에 주문이 실패하면 메시지는 전송하지 않는다.
- 또한 `OrderService`에서 주문이 완료한 이후 주문 이벤트만 전달하기 때문에 주문 서비스를 개발하는 사람은 주문 완료이후에 대한 행위를 전부 인지할 필요가 없어지고, 메시지 서비스가 수정된다고하여 주문
  서비스를 수정해야할 이유가 없어진다.
- `MessageTransactionalEventListener`는 "Spring Web의 컨트롤러처럼 외부 요청에 대해 어떤 일을 수행할지?"와 같은 역할을 수행하게 된다.

> > https://brunch.co.kr/@data-villain/24
> https://docs.spring.io/spring-framework/docs/5.3.32/reference/html/web.html#websocket-stomp <br/.
> https://sooolog.dev/HTTP-%ED%86%B5%EC%8B%A0%EA%B3%BC-TCP-%ED%86%B5%EC%8B%A0-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EC%9B%B9-%EC%86%8C%EC%BC%93%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B8%B0%EB%B3%B8-%EA%B0%9C%EB%85%90-%EC%A0%95%EB%A6%AC/