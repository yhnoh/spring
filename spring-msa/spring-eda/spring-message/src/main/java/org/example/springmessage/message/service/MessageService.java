package org.example.springmessage.message.service;

public interface MessageService {
    void sendEmail(long orderId);

    void sendkakaoTalk(long orderId);
}
