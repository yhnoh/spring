package org.example.springbatchitemwriterjpa;

import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaEntity;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberOrderCountItemWriter implements ItemWriter<MemberJpaEntity> {

    private final OrderService orderService;
    private final ItemWriter<MemberJpaEntity> helloJpaItemWriter;

    @Override
    public void write(Chunk<? extends MemberJpaEntity> chunk) throws Exception {
        List<Long> memberIds = chunk.getItems().stream().map(MemberJpaEntity::getId).collect(Collectors.toList());
        List<OrderJpaEntity> orderJpaEntities = orderService.findAllByMemberIdIn(memberIds);
        Map<Long, List<OrderJpaEntity>> orderJpaEntityMap = orderJpaEntities.stream().collect(Collectors.groupingBy(OrderJpaEntity::getMemberId));

        for (MemberJpaEntity memberJpaEntity : chunk) {
            int orderCount = orderJpaEntityMap.containsKey(memberJpaEntity.getId()) ? orderJpaEntityMap.get(memberJpaEntity.getId()).size() : 0;
            memberJpaEntity.changeOrderCount(orderCount);
        }

        helloJpaItemWriter.write(chunk);
    }
}