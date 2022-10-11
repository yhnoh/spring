package com.example.springthreadpool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DefaultAsyncConfig.class)
public class DefaultAsyncTest {

    @Autowired
    DefaultAsyncService defaultAsyncService;

    @Test
    public void asyncTest() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                try {
                    defaultAsyncService.defaultAsync();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
        Thread.sleep(10000);
    }

    @Test
    public void defaultAsyncRequestTest() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/default-async";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    }
}
