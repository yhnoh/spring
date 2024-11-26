package org.example.helloworld.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Repository
public class OrderApiRepository {

    private final WebClient webClient;

    public OrderApiRepository(WebClient.Builder webClientBuilder, @Value("${routers.order.url}") String url) {
        webClient = webClientBuilder.baseUrl(url).defaultHeaders(httpHeaders -> {
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }).build();
    }

    public Mono<List<Order>> getOrders() {


        return webClient.get()
                .retrieve()
                .bodyToFlux(Order.class)
                .collectList();

    }

    public Mono<Order> getOrdersById(long id) {

        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Order.class);
    }
}

