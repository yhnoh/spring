package org.example.springbatchitemwriterjpa;

import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.springbatchitemwriterjpa.jpa.MemberJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME;

@Service
@Transactional(transactionManager = JPA_TRANSACTION_MANAGER_BEAN_NAME)
@RequiredArgsConstructor
class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberJpaEntity save(String name) {
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.builder().name(name).build();
        return memberJpaRepository.save(memberJpaEntity);
    }

    public List<MemberJpaEntity> findAll() {
        return memberJpaRepository.findAll();
    }
}
