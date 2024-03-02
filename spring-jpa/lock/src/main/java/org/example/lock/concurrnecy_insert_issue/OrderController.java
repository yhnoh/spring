package org.example.lock.concurrnecy_insert_issue;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private static final String HOST = "http://localhost:8080";
    private final OrderService orderService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Data
    private static class OrderRequest{
        private String orderNum;
    }

    @GetMapping("/orders")
    public List<OrderJpaEntity> getOrders(){
        return orderService.getOrders();
    }

    @PostMapping("/orders")
    public void order(@RequestBody OrderRequest orderRequest){
        orderService.order(orderRequest.getOrderNum());
    }

    @PostMapping("/orders/multi-thread")
    public void orderMultiThread(@RequestBody OrderRequest orderRequest){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        RestTemplate restTemplate = restTemplateBuilder.build();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                restTemplate.postForObject(HOST + "/orders", orderRequest, Void.class);
            });
        }

        executorService.shutdown();
    }


}
