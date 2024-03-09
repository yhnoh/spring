package org.example.springmessage.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Service
@RequiredArgsConstructor
@Async
@Transactional
public class TransactionalEventListenerMessageService implements MessageService {

    private final DefaultMessageService defaultMessageService;
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmail(long orderId) {
        defaultMessageService.sendEmail(orderId);
        defaultMessageService.sendkakaoTalk(orderId);
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendkakaoTalk(long orderId) {

    }
}
