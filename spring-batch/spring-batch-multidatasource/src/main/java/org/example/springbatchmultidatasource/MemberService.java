package org.example.springbatchmultidatasource;

import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.example.springbatchmultidatasource.jpa.MemberJpaConfig.JPA_TRANSACTION_MANAGER_BEAN_NAME;
import org.example.springbatchmultidatasource.jpa.member.MemberJpaEntity;
import org.example.springbatchmultidatasource.jpa.member.MemberJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
