package org.example.lock.concurrency_update_issue;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardReviewJpaRepository boardReviewJpaRepository;

    @PostConstruct
    void postConstruct(){
        BoardJpaEntity boardJpaEntity = new BoardJpaEntity();
        boardJpaRepository.save(boardJpaEntity);
    }

    public BoardJpaEntity getBoard(long id){
        return boardJpaRepository.findById(id).orElse(null);
    }

    public List<BoardReviewJpaEntity> getReviews(long boardId){
        return boardReviewJpaRepository.findAllByBoard_Id(boardId);
    }

    public void writeReview(long boardId){
        BoardJpaEntity board = boardJpaRepository.findById(boardId).orElseThrow(IllegalArgumentException::new);

        BoardReviewJpaEntity boardReviewJpaEntity = new BoardReviewJpaEntity(board);
        boardReviewJpaRepository.save(boardReviewJpaEntity);

        board.increaseReviewCount();
    }


}
