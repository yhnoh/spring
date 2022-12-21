package com.example.springbatchitemwriter.database_item_writer.repository;

import com.example.springbatchitemwriter.database_item_writer.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(basePackageClasses = CustomerEntity.class)
public class RepositoryJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerJpaRepository customerJpaRepository;

    @Bean
    public Job repositoryJob() {
        return jobBuilderFactory.get("repositoryJob")
                .incrementer(new RunIdIncrementer())
                .start(repositoryStep())
                .build();
    }

    @Bean
    public Step repositoryStep() {
        return stepBuilderFactory.get("repositoryStep")
                .<CustomerEntity, CustomerEntity>chunk(10)
                .reader(this.repositoryItemReader(null))
                .writer(this.repositoryItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerEntity> repositoryItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<CustomerEntity>()
                .name("repositoryItemReader")
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
    public RepositoryItemWriter<CustomerEntity> repositoryItemWriter(EntityManagerFactory entityManagerFactory) {

        return new RepositoryItemWriterBuilder<CustomerEntity>()
                .repository(customerJpaRepository)
                .methodName("save")
                .build();
    }
}
