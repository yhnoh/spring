package org.example.helloworld;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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

        //요청 처리
        ServerHttpRequest request = exchange.getRequest();

        String correlationId = "";
        if (this.hasCorrectionId(request)) {
            correlationId = filterUtils.getCorrelationId(request);
            log.info("tmx-correlation-id found in tracking filter: {}. ", correlationId);
        } else {
            exchange = filterUtils.setCorrelationId(exchange, this.generateCorrectionId());
            correlationId = filterUtils.getCorrelationId(request);
            log.info("tmx-correlation-id generate in tracking filter: {}. ", correlationId);
        }

        ServerWebExchange finalExchange = exchange;
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    //응답 처리
                    log.info("tmx-correlation-id in response: {}", filterUtils.getCorrelationId(request));
                    finalExchange.getResponse().getHeaders()
                            .add(FilterUtils.CORRELATION_ID, filterUtils.getCorrelationId(request));
                }));
    }

    private boolean hasCorrectionId(ServerHttpRequest request) {
        return filterUtils.getCorrelationId(request) != null;
    }

    private String generateCorrectionId() {
        return UUID.randomUUID().toString();
    }
}
