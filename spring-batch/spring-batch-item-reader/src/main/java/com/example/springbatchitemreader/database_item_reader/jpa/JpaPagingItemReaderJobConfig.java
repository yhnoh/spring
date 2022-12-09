package com.example.springbatchitemreader.database_item_reader.jpa;


import com.example.springbatchitemreader.database_item_reader.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JpaPagingItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(this.jobPagingItemReaderStep())
                .build();
    }

    private Step jobPagingItemReaderStep() {
        return stepBuilderFactory.get("jobPagingItemReaderStep")
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(this.jpaPagingItemReader(null, null))
                .writer(this.jpaPagingItemWriter())
                .build();

    }

    @Bean
    @StepScope
    public JpaPagingItemReader<CustomerEntity> jpaPagingItemReader(EntityManager entityManager, @Value("#{jobParameters['city']}") String city) {

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManager.getEntityManagerFactory())
                .queryString("select c from CustomerEntity c where c.city = :city")
                .parameterValues(Collections.singletonMap("city", city))
                .build();

    }

    @Bean
    public ItemWriter<CustomerEntity> jpaPagingItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
