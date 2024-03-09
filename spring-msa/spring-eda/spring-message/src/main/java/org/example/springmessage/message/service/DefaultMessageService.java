package org.example.springmessage.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springmessage.message.repository.EmailJpaEntity;
import org.example.springmessage.message.repository.EmailJpaRepository;
import org.example.springmessage.message.repository.KakaoTalkJpaEntity;
import org.example.springmessage.message.repository.KakaoTalkJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class DefaultMessageService implements MessageService {

    private final EmailJpaRepository emailJpaRepository;
    private final KakaoTalkJpaRepository kakaoTalkJpaRepository;

    public List<EmailJpaEntity> getEmails(){
        return emailJpaRepository.findAll();
    }

    @Override
    public void sendEmail(long orderId){

        String message = MessageFormat.format("주문번호 {0}에 대해서 이메일을 전송하였습니다.", orderId);
        log.info(message);
        EmailJpaEntity emailJpaEntity = EmailJpaEntity.builder().orderId(orderId).message(message).build();
        emailJpaRepository.save(emailJpaEntity);
    }

    public List<KakaoTalkJpaEntity> getkakaoTalks(){
        return kakaoTalkJpaRepository.findAll();
    }

    @Override
    public void sendkakaoTalk(long orderId){
        String message = MessageFormat.format("주문번호 {0}에 대해서 카카오톡을 전송하였습니다.", orderId);
        log.info(message);
        KakaoTalkJpaEntity kakaoTalkJpaEntity = KakaoTalkJpaEntity.builder().orderId(orderId).message(message).build();
        kakaoTalkJpaRepository.save(kakaoTalkJpaEntity);
    }

}
