package com.example.springbatchitemprocessor.validate_item_processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.Collection;


@RequiredArgsConstructor
@Configuration
public class ValidateItemProcessorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validateItemProcessorJob(){
        return jobBuilderFactory.get("validateItemProcessorJob")
                .start(this.validateItemProcessorStep())
                .build();
    }


    @Bean
    public Step validateItemProcessorStep(){
        return stepBuilderFactory.get("validateItemProcessorStep")
                .<Customer, Customer>chunk(10)
                .reader(this.validateItemReader(null))
                .processor(this.validateItemProcessor())
                .writer(this.validateItemWriter())
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> validateItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){


        return new FlatFileItemReaderBuilder<Customer>()
                .name("validateItemReader")
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
    public ItemWriter<Customer> validateItemWriter(){
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public BeanValidatingItemProcessor<Customer> validateItemProcessor(){
        return new BeanValidatingItemProcessor<>();
    }
}
