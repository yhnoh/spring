package org.example.userservice.member;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public void signUp(String username, String password, String role) {
        Member member = Member.builder().build();
        member.signUp(username, passwordEncoder.encode(password), role);

        MemberJpaEntity memberJpaEntity = MemberJpaEntity.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
        memberJpaRepository.save(memberJpaEntity);
    }

}
