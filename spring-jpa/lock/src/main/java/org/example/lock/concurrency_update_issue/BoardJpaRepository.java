package org.example.lock.concurrency_update_issue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
}
