package com.example.springbatchitemreader.database_item_reader.jpa_query_provider;


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
public class JpaQueryProviderItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job jpaQueryProviderItemReaderJob() {
        return jobBuilderFactory.get("jpaQueryProviderItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(this.jpaQueryProviderItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaQueryProviderItemReaderStep() {
        return stepBuilderFactory.get("jpaQueryProviderItemReaderStep")
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(this.jpaQueryProviderItemReader(null, null))
                .writer(this.jpaQueryProviderItemWriter())
                .build();

    }

    @Bean
    @StepScope
    public JpaPagingItemReader<CustomerEntity> jpaQueryProviderItemReader(EntityManager entityManager, @Value("#{jobParameters['city']}") String city) {
        JpaQueryProvider jpaQueryProvider = new JpaQueryProvider();
        jpaQueryProvider.setCityName(city);

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("jpaQueryProviderItemReader")
                .entityManagerFactory(entityManager.getEntityManagerFactory())
                .queryProvider(jpaQueryProvider)
                .parameterValues(Collections.singletonMap("city", city))
                .build();

    }

    @Bean
    public ItemWriter<CustomerEntity> jpaQueryProviderItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
