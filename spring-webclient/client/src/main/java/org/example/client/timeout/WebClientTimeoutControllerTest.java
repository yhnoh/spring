package org.example.client.timeout;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
public class WebClientTimeoutControllerTest {

    private WebClient webClient() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8080")
                .defaultHeaders(header -> header.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }


    @GetMapping("/v1/client/timeout")
    public ClientTimeoutResponse clientTimeoutV1() {
        Mono<ClientTimeoutResponse> mono = this.webClient().get()
                .uri("/v1/server/timeout")
                .retrieve()
                .bodyToMono(ClientTimeoutResponse.class);
        return mono.block();
    }

    @GetMapping("/v2/client/timeout")
    public ClientTimeoutResponse clientTimeoutV2() {
        ResponseEntity<ClientTimeoutResponse> response = this.webClient().get()
                .uri("/v1/server/timeout")
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.toEntity(ClientTimeoutResponse.class);
                    }
                    return clientResponse.toEntity(ClientTimeoutResponse.class);
                }).onErrorReturn(ResponseEntity.ok(new ClientTimeoutResponse()))
                .onErrorResume(ReadTimeoutException.class, e -> {
                    log.error("ReadTimeoutException 발생: {}", e.getMessage());
                    return Mono.just(ResponseEntity.ok(new ClientTimeoutResponse()));
                }).block();


        return response.getBody();
    }

}
