package com.example.springbatchitemprocessor.composite_item_processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.ScriptItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class CompositeItemProcessorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job compositeItemProcessorJob(){
        return jobBuilderFactory.get("compositeItemProcessorJob")
                .start(this.compositeItemProcessorStep())
                .build();
    }

    @Bean
    public Step compositeItemProcessorStep(){
        return stepBuilderFactory.get("compositeItemProcessorStep")
                .<Customer,Customer>chunk(5)
                .reader(this.compositeItemReader(null))
                .processor(this.compositeItemProcessor())
                .writer(this.compositeItemWriter())
                .stream(this.uniqueLastNameValidator())
                .build();

    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> compositeItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){

        return new FlatFileItemReaderBuilder<Customer>()
                .name("compositeItemReader")
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
    public ItemWriter<Customer> compositeItemWriter(){
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public UniqueLastNameValidator uniqueLastNameValidator(){
        UniqueLastNameValidator uniqueLastNameValidator = new UniqueLastNameValidator();
        uniqueLastNameValidator.setName("uniqueLastNameValidator");
        return uniqueLastNameValidator;
    }

    @Bean
    @StepScope
    public ScriptItemProcessor<Customer, Customer> upperCaseItemProcessor(@Value("#{jobParameters['script']}") Resource script){
        ScriptItemProcessor<Customer, Customer> itemProcessor = new ScriptItemProcessor<>();
        itemProcessor.setScript(script);
        return itemProcessor;
    }

    @Bean
    public ValidatingItemProcessor<Customer> uniqueLastNameValidatingItemProcessor(){
        ValidatingItemProcessor<Customer> itemProcessor = new ValidatingItemProcessor<Customer>(this.uniqueLastNameValidator());
        //유효성 검증을 통과하지 못한 아이템을 걸러낸다.
        itemProcessor.setFilter(true);
        return itemProcessor;
    }

    @Bean
    public CompositeItemProcessor<Customer, Customer> compositeItemProcessor(){
        CompositeItemProcessor<Customer, Customer> itemProcessor = new CompositeItemProcessor<>();
        itemProcessor.setDelegates(List.of(uniqueLastNameValidatingItemProcessor(),
                upperCaseItemProcessor(null)));
        return itemProcessor;
    }
}
