package org.example.userservice.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Member> getMember(long id) {
        return memberJpaRepository.findById(id)
                .map(memberMapper::toMember);
    }

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
