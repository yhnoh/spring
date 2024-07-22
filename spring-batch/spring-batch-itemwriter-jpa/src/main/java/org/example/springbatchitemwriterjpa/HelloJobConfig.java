package org.example.springbatchitemwriterjpa;

import java.util.List;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderItemJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderItemJpaRepository;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaEntity;
import org.example.springbatchitemwriterjpa.jpa.order.OrderJpaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class HelloJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final OrderService orderService;


    @Bean
    public Job helloJob() {
        return new JobBuilder("helloJob", jobRepository).start(helloStep()).build();
    }


    @Bean
    public Step helloStep() {
        return new StepBuilder("helloStep", jobRepository).<MemberJpaEntity, MemberJpaEntity>chunk(1,
                platformTransactionManager).reader(this.helloItemReader(null)).writer(this.helloItemWriter()).build();
    }


    @Bean
    public JpaCursorItemReader<MemberJpaEntity> helloItemReader(
            @Qualifier("memberEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaCursorItemReaderBuilder<MemberJpaEntity>().name("helloItemReader")
                .entityManagerFactory(entityManagerFactory).queryString("select m from MemberJpaEntity m").build();
    }


    @Bean
    public ItemWriter<MemberJpaEntity> helloItemWriter() {
        return chunk -> {
            chunk.getItems().forEach(memberJpaEntity -> {
                System.out.println("memberJpaEntity.getId() = " + memberJpaEntity.getId());
                List<OrderJpaEntity> orderJpaEntities = orderService.findAll();
                orderJpaEntities.forEach(orderJpaEntity -> {
                    System.out.println("orderJpaEntity.getId() = " + orderJpaEntity.getId());
                    List<OrderItemJpaEntity> orderItems = orderJpaEntity.getOrderItems();
                    orderItems.forEach(orderItem -> {
                        System.out.println("orderItem.getId() = " + orderItem.getId());
                    });
                });
                //                orderService.save(memberJpaEntity.getId());
            });
        };
    }
}
