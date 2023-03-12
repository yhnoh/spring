package com.example.springbatchdatabase.job;

import com.example.springbatchdatabase.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class JpaPagingJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaPagingJob() {
        return jobBuilderFactory.get("jpaPagingJob")
                .start(this.jpaPagingStep())
                .build();
    }

    @Bean
    public Step jpaPagingStep() {
        return stepBuilderFactory.get("jpaPagingStep")
                .<Member, Member>chunk(20)
                .reader(this.jpaPagingItemReader())
                .writer(this.jpaPagingItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Member> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Member>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(20)
                .queryString("select m from Member m")
                .build();
    }

    @Bean
    public ItemWriter<Member> jpaPagingItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
