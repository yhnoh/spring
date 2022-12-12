package com.example.springbatchitemprocessor.script_item_processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ScriptItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
@Configuration
public class ScriptItemProcessorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job scriptItemProcessorJob(){
        return jobBuilderFactory.get("scriptItemProcessorJob")
                .start(this.scriptItemProcessorStep())
                .build();
    }


    @Bean
    public Step scriptItemProcessorStep(){
        return stepBuilderFactory.get("scriptItemProcessorStep")
                .<Customer, Customer>chunk(10)
                .reader(this.scriptItemReader(null))
                .processor(this.scriptItemProcessor(null))
                .writer(this.scriptItemWriter())
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> scriptItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){


        return new FlatFileItemReaderBuilder<Customer>()
                .name("scriptItemReader")
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
    public ItemWriter<Customer> scriptItemWriter() {
        return items -> items.forEach(System.out::println);

    }
    @Bean
    @StepScope
    public ScriptItemProcessor<Customer, Customer> scriptItemProcessor(@Value("#{jobParameters['script']}") Resource resource) {
        ScriptItemProcessor<Customer, Customer> itemProcessor = new ScriptItemProcessor<>();
        itemProcessor.setScript(resource);
        return itemProcessor;
    }


}
