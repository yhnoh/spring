package com.example.springbatchitemprocessor.custom_item_processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
@Configuration
public class CustomItemProcessorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job customItemProcessorJob(){
        return jobBuilderFactory.get("customItemProcessorJob")
                .start(this.customItemProcessorStep())
                .build();
    }

    @Bean
    public Step customItemProcessorStep(){
        return stepBuilderFactory.get("customItemProcessorStep")
                .<Customer, Customer>chunk(5)
                .reader(this.customItemReader(null))
                .processor(this.evenFilteringItemProcessor())
                .writer(this.customerItemWriter())
                .build();

    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){

        return new FlatFileItemReaderBuilder<Customer>()
                .name("customItemReader")
                .delimited()
                .names(new String[] {"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .resource(customerFile)
                .build();
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter(){
        return items -> items.forEach(System.out::println);
    }
    @Bean
    public EvenFilteringItemProcessor evenFilteringItemProcessor(){
        return new EvenFilteringItemProcessor();
    }
}
