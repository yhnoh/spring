package org.example.springwebsocket.member;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberUseCase {

    private final MemberJpaRepository memberJpaRepository;

    public void signUp(String email, String nickname) {

        memberJpaRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다. 이메일: " + member.getEmail());
                });

        memberJpaRepository.findByNickname(nickname)
                .ifPresent(member -> {
                    throw new IllegalArgumentException("이미 존재하는 닉네임입니다. 닉네임: " + member.getNickname());
                });

        MemberJpaEntity entity = new MemberJpaEntity();
        entity.setEmail(email);
        entity.setNickname(nickname);
        memberJpaRepository.save(entity);
    }
}
