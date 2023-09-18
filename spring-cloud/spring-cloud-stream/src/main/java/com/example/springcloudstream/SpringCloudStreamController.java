package com.example.springcloudstream;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;

import java.util.function.Consumer;

@RestController
@RequiredArgsConstructor
public class SpringCloudStreamController {

    private final StreamBridge streamBridge;
    @PostMapping
    public boolean test(){

        Member member = Member.builder().name("name1").age(1).build();
        boolean send = streamBridge.send("producer-out-0", member);
        return send;
    }

    @Bean
    public Consumer<Member> consumer(){
        return System.out::println;
    }
    @Getter
    @Builder
    @ToString
    static class Member{
        private String name;
        private int age;
    }
}
