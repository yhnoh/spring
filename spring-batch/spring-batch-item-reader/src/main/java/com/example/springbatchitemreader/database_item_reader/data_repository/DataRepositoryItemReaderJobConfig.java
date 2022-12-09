package com.example.springbatchitemreader.database_item_reader.data_repository;

import com.example.springbatchitemreader.database_item_reader.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class DataRepositoryItemReaderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerEntityJpaRepository customerEntityJpaRepository;

    @Bean
    public Job dataRepositoryItemReaderJob() {
        return jobBuilderFactory.get("dataRepositoryItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(this.dataRepositoryItemReaderStep())
                .build();
    }

    @Bean
    public Step dataRepositoryItemReaderStep() {
        return stepBuilderFactory.get("dataRepositoryItemReaderStep")
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(this.dataRepositoryItemReader(null))
                .writer(this.dataRepositoryItemWriter())
                .build();

    }

    @Bean
    @StepScope
    public RepositoryItemReader<CustomerEntity> dataRepositoryItemReader(@Value("#{jobParameters['city']}") String city) {

        return new RepositoryItemReaderBuilder<CustomerEntity>()
                .name("dataRepositoryItemReader")
                .arguments(Collections.singletonList(city))
                .repository(customerEntityJpaRepository)
                .methodName("findByCity")
                .sorts(Collections.singletonMap("lastName", Sort.Direction.DESC))
                .build();

    }

    @Bean
    public ItemWriter<CustomerEntity> dataRepositoryItemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
