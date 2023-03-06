package com.example.springmsaresilience4j;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository = new MemberRepository();

    private void waitRandom() {
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        if (randomNum == 3) {
            try {
                Thread.sleep(5000);
                throw new RuntimeException();
            } catch (InterruptedException e) {
                log.error("timeout");
            }
        }
    }

    @CircuitBreaker(name = "memberService")
    public List<Member> getMembers() {
        this.waitRandom();
        return memberRepository.findAll();
    }

    private List<Member> fallbackMethod(Throwable throwable) {
        return memberRepository.findAll().stream().filter(member -> member.getId().equals(1L)).collect(Collectors.toList());
    }


}
