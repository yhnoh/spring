package org.example.helloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrackingFilter implements GlobalFilter {

    private final FilterUtils filterUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        if (this.hasCorrectionId(request)) {
            log.info("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId(request));
        } else {

            exchange = filterUtils.setCorrelationId(exchange, this.generateCorrectionId());
            log.info("tmx-correlation-id generate in tracking filter: {}. ", filterUtils.getCorrelationId(request));
        }

        return chain.filter(exchange);
    }

    private boolean hasCorrectionId(ServerHttpRequest request) {
        return filterUtils.getCorrelationId(request) != null;
    }

    private String generateCorrectionId() {
        return UUID.randomUUID().toString();
    }
}
