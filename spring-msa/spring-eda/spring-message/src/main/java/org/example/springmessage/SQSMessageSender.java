package org.example.springmessage;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SQSMessageSender {

    private final QueueMessagingTemplate queueMessagingTemplate;

    public void send(String queueName, String message){
        queueMessagingTemplate.convertAndSend(queueName, message);

    }
}
