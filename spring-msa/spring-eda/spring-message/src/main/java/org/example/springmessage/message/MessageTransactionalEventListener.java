package org.example.springmessage.message;

import lombok.RequiredArgsConstructor;
import org.example.springmessage.message.service.DefaultMessageService;
import org.example.springmessage.order.event.OrderCompletedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
