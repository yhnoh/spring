package com.example.springbatchitemprocessor.existing_service_item_processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
@Configuration
public class ExistingServiceItemProcessorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ExistingService existingService;
    @Bean
    public Job existingServiceItemProcessorJob(){
        return jobBuilderFactory.get("existingServiceItemProcessorJob")
                .start(this.existingServiceItemProcessorStep())
                .build();
    }


    @Bean
    public Step existingServiceItemProcessorStep(){
        return stepBuilderFactory.get("existingServiceItemProcessorStep")
                .<Customer, Customer>chunk(10)
                .reader(this.existingServiceItemReader(null))
                .processor(this.existingServiceItemProcessor())
                .writer(this.existingServiceItemWriter())
                .build();
    }

    private ItemProcessorAdapter<Customer,Customer> existingServiceItemProcessor() {
        ItemProcessorAdapter<Customer, Customer> adapter = new ItemProcessorAdapter<>();
        adapter.setTargetObject(existingService);
        adapter.setTargetMethod("upperCase");
        return adapter;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Customer> existingServiceItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){


        return new FlatFileItemReaderBuilder<Customer>()
                .name("existingServiceItemReader")
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
    public ItemWriter<Customer> existingServiceItemWriter(){
        return items -> items.forEach(System.out::println);
    }    
}
