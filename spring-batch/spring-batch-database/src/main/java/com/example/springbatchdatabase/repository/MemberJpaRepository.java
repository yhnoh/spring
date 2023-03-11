package com.example.springbatchdatabase.repository;

import com.example.springbatchdatabase.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
