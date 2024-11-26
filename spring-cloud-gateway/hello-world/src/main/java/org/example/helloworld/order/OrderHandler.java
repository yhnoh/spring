package org.example.helloworld.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHandler {

    private final OrderApiRepository orderApiRepository;

    public Mono<ServerResponse> getOrders() {
        Mono<List<Order>> orders = orderApiRepository.getOrders();
        return orders.flatMap(order -> ServerResponse.ok().bodyValue(order));
    }

    public Mono<ServerResponse> getOrderById(ServerRequest request) {
        long id = Long.parseLong(request.pathVariable("id"));
        return orderApiRepository.getOrdersById(id).flatMap(order -> ServerResponse.ok().bodyValue(order));
    }

}
