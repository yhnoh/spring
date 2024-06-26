package com.example.springbatchdatabase.job;

import com.example.springbatchdatabase.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class JpaCursorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaCursorJob() {
        return jobBuilderFactory.get("jpaCursorJob")
                .start(this.jpaCursorStep())
                .build();
    }

    @Bean
    public Step jpaCursorStep() {
        return stepBuilderFactory.get("jpaCursorStep")
                .<Member, Member>chunk(20)
                .reader(this.jpaCursorItemReader())
                .writer(this.jpaCursorItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Member> jpaCursorItemReader() {
        return new JpaCursorItemReaderBuilder<Member>().name("jpaCursorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select m from Member m")
                .build();
    }

    @Bean
    public ItemWriter<Member> jpaCursorItemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
