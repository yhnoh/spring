package org.example.springmessage.message;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.springmessage.message.repository.EmailJpaEntity;
import org.example.springmessage.message.repository.KakaoTalkJpaEntity;
import org.example.springmessage.message.service.DefaultMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final DefaultMessageService defaultMessageService;

    @Data
    public static class OrderCompletedRequest {
        private long orderId;
    }

    @PostMapping("/emails")
    public void sendEmail(@RequestBody OrderCompletedRequest orderCompletedRequest) {
        defaultMessageService.sendEmail(orderCompletedRequest.getOrderId());
    }

    @PostMapping("/kakao-talks")
    public void sendkakaoTalk(@RequestBody OrderCompletedRequest orderCompletedRequest) {
        defaultMessageService.sendkakaoTalk(orderCompletedRequest.getOrderId());
    }

    @GetMapping("/emails")
    public List<EmailJpaEntity> getEmails() {
        return defaultMessageService.getEmails();
    }

    @GetMapping("/kakao-talks")
    public List<KakaoTalkJpaEntity> getKakaoTalks() {
        return defaultMessageService.getkakaoTalks();
    }
}
