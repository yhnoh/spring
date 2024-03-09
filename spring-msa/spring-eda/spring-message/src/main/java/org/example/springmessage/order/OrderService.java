package org.example.springmessage.order;

import lombok.RequiredArgsConstructor;
import org.example.springmessage.message.service.AsyncMessageService;
import org.example.springmessage.message.service.ExceptionMessageService;
import org.example.springmessage.order.event.OrderCompletedEvent;
import org.example.springmessage.order.event.OrderCompletedHttpEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final ExceptionMessageService exceptionMessageService;
    private final AsyncMessageService asyncMessageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<OrderJpaEntity> getOrders() {
        return orderJpaRepository.findAll();
    }

    //메시지를 보내는 것이 주문에 영향을 미친다.
    public void orderSync(String name) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        exceptionMessageService.sendEmail(orderJpaEntity.getId());
        exceptionMessageService.sendkakaoTalk(orderJpaEntity.getId());
    }

    public void orderAsync(String name) {

        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        asyncMessageService.sendEmail(orderJpaEntity.getId());
        asyncMessageService.sendkakaoTalk(orderJpaEntity.getId());

    }

    public void orderAsyncThrowException(String name) {

        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        asyncMessageService.sendEmail(orderJpaEntity.getId());
        asyncMessageService.sendkakaoTalk(orderJpaEntity.getId());

        throw new IllegalArgumentException("주문 실패");
    }

    public void orderTransactionalEventListener(String name) {

        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        OrderCompletedEvent orderCompletedEvent = new OrderCompletedEvent(this, orderJpaEntity.getId());
        applicationEventPublisher.publishEvent(orderCompletedEvent);
    }

    public void orderTransactionalEventListenerThrowException(String name) {

        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        OrderCompletedEvent event = new OrderCompletedEvent(this, orderJpaEntity.getId());
        applicationEventPublisher.publishEvent(event);

        throw new IllegalArgumentException("주문 실패");
    }

    public void orderHttp(String name) {
        OrderJpaEntity orderJpaEntity = OrderJpaEntity.builder().name(name).build();
        orderJpaRepository.save(orderJpaEntity);

        OrderCompletedHttpEvent event = new OrderCompletedHttpEvent(this, orderJpaEntity.getId());
        applicationEventPublisher.publishEvent(event);
    }
}
