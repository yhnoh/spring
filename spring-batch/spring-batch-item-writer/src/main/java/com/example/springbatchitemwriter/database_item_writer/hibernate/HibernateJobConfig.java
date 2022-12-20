package com.example.springbatchitemwriter.database_item_writer.hibernate;

import com.example.springbatchitemwriter.database_item_writer.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.database.builder.HibernateItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
public class HibernateJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
//    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job hibernateJob() {
        return jobBuilderFactory.get("hibernateJob")
                .incrementer(new RunIdIncrementer())
                .start(hibernateStep())
                .build();
    }

    @Bean
    public Step hibernateStep() {
        return stepBuilderFactory.get("hibernateStep")
                .<CustomerEntity, CustomerEntity>chunk(10)
                .reader(this.hibernateItemReader(null))
                .writer(this.hibernateItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerEntity> hibernateItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<CustomerEntity>()
                .name("hibernateItemReader")
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
    public HibernateItemWriter<CustomerEntity> hibernateItemWriter(EntityManagerFactory entityManagerFactory) {

        return new HibernateItemWriterBuilder<CustomerEntity>()
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .build();
    }
}
