package org.example.springbatchmultidatasource.jpa.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    List<OrderJpaEntity> findAllByMemberId(long memberId);

    List<OrderJpaEntity> findAllByMemberIdIn(Iterable<Long> memberIds);
}
