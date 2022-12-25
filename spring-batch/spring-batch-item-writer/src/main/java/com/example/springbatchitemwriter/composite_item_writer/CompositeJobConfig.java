package com.example.springbatchitemwriter.composite_item_writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class CompositeJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job compositeJob() {
        return jobBuilderFactory.get("compositeJob")
                .incrementer(new RunIdIncrementer())
                .start(this.compositeStep())
                .build();
    }

    @Bean
    public Step compositeStep() {
        return stepBuilderFactory.get("compositeStep")
                .<Customer, Customer>chunk(10)
                .reader(this.compositeItemReader(null))
                .writer(this.compositeItemWriter())
                .build();
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Customer> compositeItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("compositeItemReader")
                .resource(inputFile)
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .build();
    }

    /**
     * <pre>CompositeItemWriter</pre>
     * <p>
     * CompositeItemWriter를 활용하면 스텝에서 각 아이템을 처리할 때 여러 장소에 쓰기 작업을 할 수 있다.
     */
    @Bean
    public CompositeItemWriter<Customer> compositeItemWriter() {

        return new CompositeItemWriterBuilder<Customer>()
                .delegates(this.compositeJdbcItemWriter(),
                        this.compositeFileItemWriter(null))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> compositeJdbcItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("INSERT INTO CUSTOMER (first_name, " +
                        "middle_initial, " +
                        "last_name, " +
                        "address, " +
                        "city, " +
                        "state, " +
                        "zip) VALUES (:firstName, :middleInitial, :lastName, :address, :city, :state, :zip)")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> compositeFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile) {
        return new FlatFileItemWriterBuilder<Customer>()
                .name("compositeFileItemWriter")
                .resource(outputFile)
                .formatted()
                .format("%s %s lives at %s %s in %s, %s.")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
                .build();
    }

}
