package org.example.userservice.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signUp(String username, String password, String role) {
        Member member = Member.builder().build();
        member.signUp(username, passwordEncoder.encode(password), role);

        MemberJpaEntity memberJpaEntity = MemberJpaEntity.builder()
                .loginId(member.getLoginId())
                .loginPassword(member.getLoginPassword())
                .role(member.getRole())
                .build();
        memberJpaRepository.save(memberJpaEntity);
    }

}
