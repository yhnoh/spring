package org.example.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class BoardController {

    String host = "http://localhost:8080";

    private final BoardService boardService;
    private final RestTemplateBuilder restTemplateBuilder;

    @GetMapping("/boards")
    public List<BoardJpaEntity> getBoards(){
        return boardService.getBoards();
    }

    @GetMapping("/boards/{id}")
    public BoardJpaEntity getBoard(@PathVariable long id){
        return boardService.getBoard(id);
    }

    @PutMapping("/boards/{id}")
    public void increaseViewHit(@PathVariable long id){
        boardService.increaseViewHit(id);
    }

    @PutMapping("/boards/{id}/multi-thread")
    public void increaseViewHitConcurrency(@PathVariable long id){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        RestTemplate restTemplate = restTemplateBuilder.build();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                restTemplate.put(host + "/boards/{id}", null, id);
            });
        }

        executorService.shutdown();
    }
}
