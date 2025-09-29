package org.example.springwebsocket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final StringRedisTemplate stringRedisTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chats/{chatId}")
    public void sendChatMessage(@DestinationVariable String chatId,
                                @Payload SendChatMessage message) {
        LocalDateTime chatSendAt = LocalDateTime.now();
        String redisKeys = "chats:" + chatId;
        BoundZSetOperations<String, String> chats = stringRedisTemplate.boundZSetOps(redisKeys);
        chats.add(message.getMessage(), chatSendAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }


}