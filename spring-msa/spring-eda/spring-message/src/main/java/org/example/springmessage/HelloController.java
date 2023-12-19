package org.example.springmessage;

import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloController {

    private final SQSMessageSender sqsMessageSender;

    @GetMapping("/hello")
    public void hello(){
        sqsMessageSender.send("MyQueue", "hello");
    }

    @SqsListener("MyQueue")
    public void sqsListener(String message){
        log.info("sqsListener message = {}", message);
    }

}
