package com.example.querydsl.repository;

import com.example.querydsl.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {
}
