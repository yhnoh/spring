package org.example.springbatchjoblauncher.v2.order;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class OrderComponent2 implements OrderComponent {

    @Override
    public String getName() {
        return "orderComponent2";
    }
}
