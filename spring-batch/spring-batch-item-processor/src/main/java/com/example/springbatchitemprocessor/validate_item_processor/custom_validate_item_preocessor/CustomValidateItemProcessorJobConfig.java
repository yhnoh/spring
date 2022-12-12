package com.example.springbatchitemprocessor.validate_item_processor.custom_validate_item_preocessor;

import com.example.springbatchitemprocessor.validate_item_processor.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class CustomValidateItemProcessorJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job customValidateItemProcessorJob(){
        return jobBuilderFactory.get("customValidateItemProcessorJob")
                .start(this.customValidateItemProcessorStep())
                .build();
    }


    @Bean
    public Step customValidateItemProcessorStep(){
        return stepBuilderFactory.get("customValidateItemProcessorStep")
                .<Customer, Customer>chunk(10)
                .reader(this.customValidateItemReader(null))
                .processor(this.customValidateItemProcessor())
                .writer(this.customValidateItemWriter())
                .stream(customValidator())
                .build();
    }



    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customValidateItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){


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
    public ItemWriter<Customer> customValidateItemWriter(){
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public CustomValidator customValidator(){
        CustomValidator validator = new CustomValidator();
        validator.setName("customValidator");
        return validator;
    }

    @Bean
    public ValidatingItemProcessor<Customer> customValidateItemProcessor(){
        return new ValidatingItemProcessor<>(customValidator());
    }


}
