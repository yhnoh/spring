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
@SpringBootTest
//@ContextConfiguration(classes = AsyncConfig.class)
public class SpringThreadPoolTest {


    @Test
    public void tomcatThreadTest() throws InterruptedException {


        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {

                String url = "http://localhost:8080";
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            });
            thread.start();
        }

        Thread.sleep(10000);
    }

}
