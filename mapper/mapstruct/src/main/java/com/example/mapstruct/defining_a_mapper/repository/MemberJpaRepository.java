package com.example.mapstruct.defining_a_mapper.repository;

import com.example.mapstruct.defining_a_mapper.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
}
