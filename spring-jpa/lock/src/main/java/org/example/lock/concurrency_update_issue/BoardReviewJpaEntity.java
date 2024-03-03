package org.example.lock.concurrency_update_issue;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "board_review")
public class BoardReviewJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardJpaEntity board;

    public BoardReviewJpaEntity(BoardJpaEntity board) {
        this.board = board;
    }
}
