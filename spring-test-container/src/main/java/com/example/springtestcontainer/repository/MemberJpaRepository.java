package com.example.springtestcontainer.repository;

import com.example.springtestcontainer.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
