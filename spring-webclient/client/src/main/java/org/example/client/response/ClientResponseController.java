package org.example.client.response;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/client/response")
public class ClientResponseController {

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/server/response")
                .defaultHeaders(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    /**
     * Collection 데이터 응답
     */
    @GetMapping("/datas")
    public List<ClientResponseV1> getDatas() {
        return webClient().get()
                .uri("/datas")
                .retrieve()
                .bodyToFlux(ClientResponseV1.class)
                .collectList()
                .block();
    }

    /**
     * 단일 데이터 응답
     */
    @GetMapping("/data")
    public ClientResponseV1 getData() {
        return webClient().get()
                .uri("/data")
                .retrieve()
                .bodyToMono(ClientResponseV1.class)
                .block();
    }
}
