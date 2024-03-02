package org.example.lock;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
}
