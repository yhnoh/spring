package org.example.springmessage.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
