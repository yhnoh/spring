package org.example.springbatchmultidatasource.jpa.member;


import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {}
