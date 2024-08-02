package org.example.springbatchjoblauncher.v2.order;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderComponentTest implements InitializingBean {

    private final List<OrderComponent> orderComponentList;
    private final Collection<OrderComponent> orderComponentCollection;


    @Override
    public void afterPropertiesSet() throws Exception {
        for(OrderComponent orderComponent : orderComponentList) {
            System.out.println("orderComponentList item = " + orderComponent.getName());
        }

        for(OrderComponent orderComponent : orderComponentCollection) {
            System.out.println("orderComponentCollection item = " + orderComponent.getName());
        }
    }
}
