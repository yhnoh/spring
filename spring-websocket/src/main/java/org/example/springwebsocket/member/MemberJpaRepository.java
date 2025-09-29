package org.example.springwebsocket.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {

    Optional<MemberJpaEntity> findByEmail(String email);

    Optional<MemberJpaEntity> findByNickname(String nickname);
}
