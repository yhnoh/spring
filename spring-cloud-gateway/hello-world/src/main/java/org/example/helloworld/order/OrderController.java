package org.example.helloworld.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OrderController {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(OrderHandler orderHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/v1/orders"), request -> orderHandler.getOrders())
                .andRoute(RequestPredicates.GET("/v1/orders/{id}"), orderHandler::getOrderById);
    }

}
