package org.example.lock.concurrency_update_issue;

import lombok.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Getter
    @Setter
    @NoArgsConstructor
    static class BoardReviewResponse {
        private long id;

        public BoardReviewResponse(long id) {
            this.id = id;
        }
    }


    @GetMapping("/boards/{id}")
    public BoardJpaEntity getBoard(@PathVariable long id){
        return boardService.getBoard(id);
    }

    @GetMapping("/boards/{id}/reviews")
    public List<BoardReviewResponse> getReviews(@PathVariable long id){
        return boardService.getReviews(id).stream()
                .map(boardReview -> new BoardReviewResponse(boardReview.getId()))
                .collect(Collectors.toList());
    }

    @PostMapping("/boards/{id}/reviews")
    public void writeReview(@PathVariable long id){
        boardService.writeReview(id);
    }

    @PostMapping("/boards/{id}/reviews/multi-thread")
    public void writeReviewMultiThread(@PathVariable long id){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        RestTemplate restTemplate = restTemplateBuilder.build();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                restTemplate.postForObject("http://localhost:8080/boards/{id}/reviews", null,
                        Void.class, id);
            });
        }

        executorService.shutdown();
    }
}
