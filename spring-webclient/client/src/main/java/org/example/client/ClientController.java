package org.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController {

    private RestTemplate restTemplate(){
        return new RestTemplateBuilder()
                .rootUri("http://localhost:8080")
                .build();
    }

    private WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeaders(header ->
                        header.setContentType(MediaType.APPLICATION_JSON)
                )
                .build();
    }

    @RequestMapping("/client")
    public void client(){
        int id = 1;
        log.info("[클라이언트] 작업 시작");
        log.info("[클라이언트-{}] 서버 요청 시작", id);
        ServerResponse serverResponse = this.webClient().get()
                .uri("/server/{id}", id)
                .retrieve()
                .bodyToMono(ServerResponse.class)
                .block();
        log.info("[클라이언트-{}] 서버 응답 값 : {}", id, serverResponse);
        log.info("[클라이언트] 작업 완료");
    }

    @RequestMapping("/client2")
    public void client2(){

        log.info("[클라이언트] 작업 시작");
        Flux<ServerResponse> flux = Flux.range(1, 10)
                .flatMap(id -> this.webClient().get()
                        .uri("/server/{id}", id)
                        .retrieve()
                        .bodyToMono(ServerResponse.class)
                ).doOnSubscribe(subscription -> log.info("[클라이언트] 서버 요청 시작"));

        Mono<List<ServerResponse>> collect = flux.collect(Collectors.toList())
                .doOnSubscribe(subscription -> log.info("[클라이언트] 서버 요청 시작"))
                .doOnSuccess(serverResponse -> log.info("[클라이언트] 서버 응답 값 : {}", serverResponse));

        List<ServerResponse> serverResponse = collect.block();

        log.info("[클라이언트] 서버 응답 값 : {}", serverResponse);

//        for (int id = 1; id <= 10; id++) {
//
//            log.info("[클라이언트-{}] 서버 요청 시작", id);
//
//            int finalId = id;
//
//            Mono<ServerResponse> serverResponseMono = this.webClient().get()
//                    .uri("/server/{id}", id)
//                    .retrieve()
//                    .bodyToMono(ServerResponse.class);
//
//            serverResponseMono.subscribe();
//        }
//
//
//
//
//        flux.subscribe(serverResponse -> log.info("[클라이언트] 서버 응답 값 : {}", serverResponse));

        log.info("[클라이언트] 작업 완료");

    }
}
