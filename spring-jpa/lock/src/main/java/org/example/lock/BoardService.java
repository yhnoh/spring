package org.example.lock;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {

    private final BoardJpaRepository boardJpaRepository;

    @PostConstruct
    void postConstruct(){
        BoardJpaEntity boardJpaEntity = new BoardJpaEntity();
        boardJpaRepository.save(boardJpaEntity);
    }

    public List<BoardJpaEntity> getBoards(){
        return boardJpaRepository.findAll();
    }

    public BoardJpaEntity getBoard(long id){
        return boardJpaRepository.findById(id).orElse(null);
    }
    public void increaseViewHit(long id){
        boardJpaRepository.findById(id).ifPresent(board -> {board.increaseViewHit();});
    }
}
