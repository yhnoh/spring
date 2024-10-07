package org.example.userservice.member;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void postConstruct() {
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .build();

        memberJpaRepository.save(memberJpaEntity);
        System.out.println("memberJpaEntity = " + memberJpaEntity);
    }
}
