package org.example.springbatchitemwriterjpa.jpa.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    List<OrderJpaEntity> findAllByMemberId(long memberId);

    List<OrderJpaEntity> findAllByMemberIdIn(Iterable<Long> memberIds);
}
