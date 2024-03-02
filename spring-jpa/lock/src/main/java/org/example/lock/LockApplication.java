package org.example.lock;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class LockApplication {

    private final MemberJpaRepository memberJpaRepository;
    public static void main(String[] args) {
        SpringApplication.run(LockApplication.class, args);
    }

    @PostConstruct
    void postConstruct() {
        MemberJpaEntity memberJpaEntity = new MemberJpaEntity("username");
        memberJpaRepository.save(memberJpaEntity);
    }
}
