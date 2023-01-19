package com.example.mapstruct.repository;

import com.example.mapstruct.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
}
