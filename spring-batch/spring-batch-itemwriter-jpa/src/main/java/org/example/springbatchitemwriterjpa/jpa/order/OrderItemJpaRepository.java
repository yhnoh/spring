package org.example.springbatchitemwriterjpa.jpa.order;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemJpaEntity, Long> {

    @EntityGraph(attributePaths = "order", type = EntityGraph.EntityGraphType.LOAD)
    List<OrderItemJpaEntity> findAll();

}
