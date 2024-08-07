package com.example.springthreadpool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AsyncConfig.class)
public class AsyncTest {

    @Autowired
    AsyncService asyncService;

    @Test
    public void taskRejectedExceptionTest() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                try {
                    asyncService.async();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
            thread.start();
        }

        Thread.sleep(10000);

    }

    @Test
    public void taskRejectedExceptionRequestTest() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {

                String url = "http://localhost:8080/async";
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            });
            thread.start();
        }

        Thread.sleep(1000);

    }

    @Test
    public void asyncThrowsTest() throws InterruptedException {
        asyncService.asyncThrows();
        Thread.sleep(10000);

    }

    @Test
    public void asyncThrowsRequestTest() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/async-throws";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    }

    @Test
    public void asyncOverThreadTest() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {

                String url = "http://localhost:8080/async-over-thread";
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            });
            thread.start();
        }

        Thread.sleep(1000);

    }

}
