package com.example.springbatchdatabase.job;

import com.example.springbatchdatabase.entity.Member;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class HibernatePagingJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job hibernatePagingJob() {
        return jobBuilderFactory.get("hibernatePagingJob")
                .start(this.hibernatePagingStep())
                .build();
    }

    @Bean
    public Step hibernatePagingStep() {
        return stepBuilderFactory.get("hibernatePagingStep")
                .<Member, Member>chunk(20)
                .reader(this.hibernatePagingItemReader())
                .writer(this.hibernatePagingItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public HibernatePagingItemReader<Member> hibernatePagingItemReader() {
        return new HibernatePagingItemReaderBuilder<Member>()
                .name("hibernatePagingItemReader")
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .pageSize(20)
                .fetchSize(20)
                .queryString("from Member")
                .build();

    }

    @Bean
    public ItemWriter<Member> hibernatePagingItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
