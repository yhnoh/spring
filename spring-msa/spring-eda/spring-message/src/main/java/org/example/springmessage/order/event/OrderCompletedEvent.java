package org.example.springmessage.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class OrderCompletedEvent extends ApplicationEvent {
    private long orderId;

    public OrderCompletedEvent(Object source, long orderId) {
        super(source);
        this.orderId = orderId;
    }
}
