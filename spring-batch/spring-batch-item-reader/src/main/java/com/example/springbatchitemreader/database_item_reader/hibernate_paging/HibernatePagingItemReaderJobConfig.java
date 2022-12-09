package com.example.springbatchitemreader.database_item_reader.hibernate_paging;

import com.example.springbatchitemreader.database_item_reader.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class HibernatePagingItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job hibernatePagingItemReaderJob() {
        return jobBuilderFactory.get("hibernatePagingItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(hibernatePagingItemReaderStep())
                .build();
    }

    @Bean
    public Step hibernatePagingItemReaderStep() {
        return stepBuilderFactory.get("hibernatePagingItemReaderStep")
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(hibernatePagingItemReader(null, null))
                .writer(hibernatePagingItemWriter())
                .build();
    }

    private ItemWriter<CustomerEntity> hibernatePagingItemWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    @StepScope
    public HibernatePagingItemReader<CustomerEntity> hibernatePagingItemReader(EntityManagerFactory entityManagerFactory,
                                                                               @Value("#{jobParameters['city']}") String city) {
        return new HibernatePagingItemReaderBuilder<CustomerEntity>()
                .name("hibernatePagingItemReader")
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .queryString("from CustomerEntity where city = :city")
                .parameterValues(Collections.singletonMap("city", city))
                .pageSize(10)
                .build();
    }

}
