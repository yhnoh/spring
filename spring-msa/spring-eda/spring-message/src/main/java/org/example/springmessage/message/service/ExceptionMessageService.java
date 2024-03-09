package org.example.springmessage.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class ExceptionMessageService implements MessageService {

    private final DefaultMessageService messageService;

    @Override
    public void sendEmail(long orderId){
        if(orderId == 2){
            String message = MessageFormat.format("주문번호 {0}여서 이메일 전송 에러가 발생하였습니다.", orderId);
            throw new IllegalArgumentException(message);
        }
        messageService.sendEmail(orderId);
    }

    @Override
    public void sendkakaoTalk(long orderId) {
        if(orderId == 2){
            String message = MessageFormat.format("주문번호 {0}여서 카카오톡 전송 에러가 발생하였습니다.", orderId);
            throw new IllegalArgumentException(message);
        }

        messageService.sendkakaoTalk(orderId);
    }
}
