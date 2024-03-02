package org.example.lock;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final RestTemplateBuilder restTemplateBuilder;
    @Data
    public static class MemberRequest{
        private long id;
        private String username;
    }

    @GetMapping
    public List<MemberJpaEntity> getMembers(){
        return memberService.getMembers();
    }

    @PutMapping
    public void changeUsername(@RequestBody MemberRequest memberRequest){
        memberService.changeUsername(memberRequest.getId(), memberRequest.getUsername());
    }

    @PutMapping("/lock")
    public void changeUsernameLock(@RequestBody MemberRequest memberRequest){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                RestTemplate build = restTemplateBuilder.build();
                build.put("http://localhost:8080/members", memberRequest);
            });
        }

        executorService.shutdown();
    }
}
