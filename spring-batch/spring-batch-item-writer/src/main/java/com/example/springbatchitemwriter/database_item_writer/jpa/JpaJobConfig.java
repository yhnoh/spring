package com.example.springbatchitemwriter.database_item_writer.jpa;

import com.example.springbatchitemwriter.database_item_writer.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
public class JpaJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
//    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job jpaJob() {
        return jobBuilderFactory.get("jpaJob")
                .incrementer(new RunIdIncrementer())
                .start(jpaStep())
                .build();
    }

    @Bean
    public Step jpaStep() {
        return stepBuilderFactory.get("jpaStep")
                .<CustomerEntity, CustomerEntity>chunk(10)
                .reader(this.jpaItemReader(null))
                .writer(this.jpaItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerEntity> jpaItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<CustomerEntity>()
                .name("jpaItemReader")
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(CustomerEntity.class)
                .resource(inputFile)
                .build();
    }

    @Bean
    public JpaItemWriter<CustomerEntity> jpaItemWriter(EntityManagerFactory entityManagerFactory) {

        return new JpaItemWriterBuilder<CustomerEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
