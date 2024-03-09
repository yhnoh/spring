package org.example.springmessage.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class OrderCompletedHttpEvent extends ApplicationEvent {
    private final long orderId;

    public OrderCompletedHttpEvent(Object source, long orderId) {
        super(source);
        this.orderId = orderId;
    }
}
