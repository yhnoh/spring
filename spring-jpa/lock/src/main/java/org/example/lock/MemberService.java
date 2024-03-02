package org.example.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    private final MemberJpaRepository memberJpaRepository;

    public List<MemberJpaEntity> getMembers(){
        return memberJpaRepository.findAll();
    }
    public void changeUsername(long id, String username){
        Optional<MemberJpaEntity> memberJpaEntity = memberJpaRepository.findById(id);
        memberJpaEntity.ifPresent(member -> {
            member.changeUsername(username);
        });
        memberJpaRepository.findById(id);

    }
}
