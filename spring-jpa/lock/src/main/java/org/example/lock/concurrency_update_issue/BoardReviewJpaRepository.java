package org.example.lock.concurrency_update_issue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReviewJpaRepository extends JpaRepository<BoardReviewJpaEntity, Long> {

    List<BoardReviewJpaEntity> findAllByBoard_Id(long id);

}
