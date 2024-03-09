package org.example.springmessage.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJpaRepository extends JpaRepository<EmailJpaEntity, Long> {
}
