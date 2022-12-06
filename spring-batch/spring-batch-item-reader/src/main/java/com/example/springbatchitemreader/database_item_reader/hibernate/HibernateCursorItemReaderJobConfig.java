package com.example.springbatchitemreader.database_item_reader.hibernate;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class HibernateCursorItemReaderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job hibernateCursorItemReaderJob() {
        return jobBuilderFactory.get("HibernateCursorItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(hibernateCursorItemReaderStep())
                .build();
    }

    @Bean
    public Step hibernateCursorItemReaderStep() {
        return stepBuilderFactory.get("hibernateCursorItemReaderStep")
                .<Customer, Customer>chunk(100)
                .reader(hibernateCursorItemReader(null, null))
                .writer(hibernateCursorItemWriter())
                .build();
    }

    private ItemWriter<Customer> hibernateCursorItemWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    @StepScope
    public HibernateCursorItemReader<Customer> hibernateCursorItemReader(EntityManagerFactory entityManagerFactory,
                                                                         @Value("#{jobParameters['city']}") String city) {
        return new HibernateCursorItemReaderBuilder<Customer>()
                .name("hibernateCursorItemReader")
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .queryString("from Customer where city = :city")
                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }
}
