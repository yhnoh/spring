package com.example.springbatchitemreader.custom_item_reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job customItemReaderJob() {
        return jobBuilderFactory.get("customItemReaderJob")
//                .incrementer(new RunIdIncrementer())
                .start(customItemReaderStep())
                .build();
    }

    @Bean
    public Step customItemReaderStep() {
        return stepBuilderFactory.get("customItemReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(customerItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<Customer> customItemWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public ItemReader<Customer> customerItemReader() {
        CustomItemReader customItemReader = new CustomItemReader();
        customItemReader.setName("customItemReader");
        return customItemReader;
    }
}
