package org.example.springmessage.message;

import lombok.RequiredArgsConstructor;
import org.example.springmessage.message.repository.EmailJpaEntity;
import org.example.springmessage.message.repository.KakaoTalkJpaEntity;
import org.example.springmessage.message.service.DefaultMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final DefaultMessageService defaultMessageService;

    @GetMapping("/emails")
    public List<EmailJpaEntity> getEmails(){
        return defaultMessageService.getEmails();
    }

    @GetMapping("/kakao-talks")
    public List<KakaoTalkJpaEntity> getKakaoTalks(){
        return defaultMessageService.getkakaoTalks();
    }
}
