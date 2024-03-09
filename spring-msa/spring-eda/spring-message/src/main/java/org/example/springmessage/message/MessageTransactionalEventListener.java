package org.example.springmessage.message;

import lombok.RequiredArgsConstructor;
import org.example.springmessage.message.service.DefaultMessageService;
import org.example.springmessage.order.event.OrderCompletedEvent;
import org.example.springmessage.order.event.OrderCompletedHttpEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Async
@RequiredArgsConstructor
public class MessageTransactionalEventListener {

    private final DefaultMessageService defaultMessageService;
    private final RestTemplateBuilder restTemplateBuilder;
    private final Environment environment;


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void completeOrder(OrderCompletedEvent orderCompletedEvent) {
        defaultMessageService.sendEmail(orderCompletedEvent.getOrderId());
        defaultMessageService.sendkakaoTalk(orderCompletedEvent.getOrderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void completeOrderHttp(OrderCompletedHttpEvent orderCompletedEvent) throws UnknownHostException {

        MessageController.OrderCompletedRequest orderCompletedRequest = new MessageController.OrderCompletedRequest();
        orderCompletedRequest.setOrderId(orderCompletedEvent.getOrderId());

        RestTemplate restTemplate = restTemplateBuilder.build();

        String host = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("local.server.port");
        String domain = "http://" + host + ":" + port;

        restTemplate.postForObject(domain + "/messages/emails", orderCompletedRequest, Void.class);
        restTemplate.postForObject(domain + "/messages/kakao-talks", orderCompletedRequest, Void.class);
    }
}
