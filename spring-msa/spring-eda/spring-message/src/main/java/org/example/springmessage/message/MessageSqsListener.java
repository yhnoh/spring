package org.example.springmessage.message;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.example.springmessage.message.service.DefaultMessageService;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MessageSqsListener {

    private final DefaultMessageService defaultMessageService;
//    @SqsListener("EMAIL")
//    public void sendEmail(@Payload EmailEvent emailEvent){
//        defaultMessageService.sendEmail(emailEvent.getOrderId());
//    }
//
//    @SqsListener("KAKAO")
//    public void sendKakao(@Payload OrderCompletedEvent orderCompletedEvent){
//        defaultMessageService.sendkakaoTalk(orderCompletedEvent.getOrderId());
//    }

}
