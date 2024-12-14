package org.example.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/webclient")
@Slf4j
public class WebClientController {


    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeaders(header ->
                        header.setContentType(MediaType.APPLICATION_JSON)
                )
                .build();
    }


    @RequestMapping("/v1")
    public ClientResponseV1 clientV1() {
        StopWatch stopWatch = this.startTask();

        log.info("[클라이언트] 서버 요청 시작");
        ClientResponseV1 response = this.webClient().get()
                .uri("/v1/server/{id}", 0)
                .retrieve()
                .bodyToMono(ClientResponseV1.class)
                .block();
        log.info("[클라이언트] 서버 응답 값 : {}", response);

        this.endTask(stopWatch);
        return response;
    }

    @RequestMapping("/v2")
    public List<ClientResponseV1> clientV2() {
        StopWatch stopWatch = this.startTask();

        List<ClientResponseV1> responses = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            log.info("[클라이언트] 서버 요청 시작");
            ClientResponseV1 response = this.webClient().get()
                    .uri("/v1/server/{id}", i)
                    .retrieve()
                    .bodyToMono(ClientResponseV1.class)
                    .block();
            log.info("[클라이언트] 서버 응답 값 : {}", response);
            responses.add(response);
        }

        this.endTask(stopWatch);
        return responses;
    }


    @RequestMapping("/v3")
    public List<ClientResponseV1> clientV3() {
        StopWatch stopWatch = this.startTask();

        Flux<ClientResponseV1> flux = Flux.range(1, 10)
                .flatMap(id -> this.webClient().get()
                        .uri("/v1/server/{id}", id)
                        .retrieve()
                        .bodyToMono(ClientResponseV1.class)
                );


        Mono<List<ClientResponseV1>> collect = flux.collectList()
                .doOnSubscribe(subscription -> log.info("[클라이언트] 서버 요청 시작"))
                .doOnSuccess(response -> log.info("[클라이언트] 서버 응답 값 : {}", response));

        List<ClientResponseV1> responses = collect.block();

        this.endTask(stopWatch);
        return responses;
    }

    @RequestMapping("/v4")
    public List<ClientResponseV1> clientV4() {
        StopWatch stopWatch = this.startTask();

        ParallelFlux<ClientResponseV1> flux = Flux.range(1, 10)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(id -> this.webClient().get()
                        .uri("/v1/server/{id}", id)
                        .retrieve()
                        .bodyToMono(ClientResponseV1.class)
                );

        Mono<List<ClientResponseV1>> collect = flux.sequential().collectList()
                .doOnSubscribe(subscription -> log.info("[클라이언트] 서버 요청 시작"))
                .doOnSuccess(serverResponse -> log.info("[클라이언트] 서버 응답 값 : {}", serverResponse));

        List<ClientResponseV1> responses = collect.block();

        this.endTask(stopWatch);
        return responses;
    }


    private StopWatch startTask() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("[클라이언트] 작업 시작");
        return stopWatch;
    }

    private void endTask(StopWatch stopWatch) {
        stopWatch.stop();
        log.info("[클라이언트] 작업 완료");
        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }

}
