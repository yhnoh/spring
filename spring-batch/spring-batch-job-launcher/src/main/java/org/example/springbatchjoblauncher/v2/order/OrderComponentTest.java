package org.example.springbatchjoblauncher.v2.order;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderComponentTest implements InitializingBean {

    private final List<OrderComponent> orderComponents;
    private final Collection<OrderComponent> orderComponentCollection;


    @Override
    public void afterPropertiesSet() throws Exception {
        for(OrderComponent orderComponent : orderComponents) {
            System.out.println("orderComponent = " + orderComponent.getName());
        }

        for(OrderComponent orderComponent : orderComponentCollection) {
            System.out.println("orderComponent = " + orderComponent.getName());
        }
    }
}
