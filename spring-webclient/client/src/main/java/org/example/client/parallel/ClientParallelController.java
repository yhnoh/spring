package org.example.client.parallel;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/client/parallel")
@Slf4j
public class ClientParallelController {

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeaders(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    @GetMapping("/sequence")
    public List<ClientParallel> requestSequence(@RequestParam int startId, @RequestParam int endId) {

        StopWatch stopWatch = this.startStopWatch();

        ArrayList<ClientParallel> list = new ArrayList<>();
        for (int i = startId; i <= endId; i++) {
            log.info("[클라이언트] 서버 요청 id={}", i);
            int finalI = i;
            ClientParallel block = webClient().get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/server/parallel/{id}")
                            .build(finalI))
                    .retrieve()
                    .bodyToMono(ClientParallel.class)
                    .block();
            list.add(block);
            log.info("[클라이언트] 서버 응답 id={}", i);
        }

        this.stopStopWatch(stopWatch);
        return list;
    }

    @GetMapping("/parallel")
    public List<ClientParallel> requestParallel(@RequestParam int startId, @RequestParam int endId) {

        StopWatch stopWatch = this.startStopWatch();

        Flux<ClientParallel> flux = Flux.range(startId, endId + 1)
                .flatMap(id -> webClient().get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/server/parallel/{id}")
                                .build(id))
                        .retrieve()
                        .bodyToMono(ClientParallel.class))
                .doOnSubscribe(subscription -> log.info("[클라이언트] 서버 요청 시작"))
                .doOnComplete(() -> log.info("[클라이언트] 서버 요청 완료"));

        List<ClientParallel> response = flux.collectList().block();

        this.stopStopWatch(stopWatch);
        return response;
    }


    private StopWatch startStopWatch() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return stopWatch;
    }

    private void stopStopWatch(StopWatch stopWatch) {
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }


}
