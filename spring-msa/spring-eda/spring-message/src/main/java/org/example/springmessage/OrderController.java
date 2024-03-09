package org.example.springmessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.springmessage.order.OrderJpaEntity;
import org.example.springmessage.order.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
class OrderController {

    private final OrderService orderService;
    @Data
    static class OrderRequest {
        private String name;
    }

    @GetMapping
    public List<OrderJpaEntity> getOrders(){
        return orderService.getOrders();
    }

    @PostMapping("/sync")
    public void orderSync(@RequestBody OrderRequest orderRequest){
        orderService.orderSync(orderRequest.getName());
    }

    @PostMapping("/async")
    public void orderAsync(@RequestBody OrderRequest orderRequest){
        orderService.orderAsync(orderRequest.getName());
    }
    @PostMapping("/async-throw-exception")
    public void orderAsyncThrowException(@RequestBody OrderRequest orderRequest){
        orderService.orderAsyncThrowException(orderRequest.getName());
    }

    @PostMapping("/transaction-event-listener")
    public void orderTransactionalEventListener(@RequestBody OrderRequest orderRequest){
        orderService.orderTransactionalEventListener(orderRequest.getName());
    }

    @PostMapping("/transaction-event-listener-throw-exception")
    public void orderTransactionalEventListenerThrowException(@RequestBody OrderRequest orderRequest){
        orderService.orderTransactionalEventListenerThrowException(orderRequest.getName());
    }

}
