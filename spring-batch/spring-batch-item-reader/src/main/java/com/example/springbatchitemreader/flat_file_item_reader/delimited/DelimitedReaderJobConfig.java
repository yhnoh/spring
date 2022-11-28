package com.example.springbatchitemreader.flat_file_item_reader.delimited;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DelimitedReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job delimitedReaderJob(){
        return jobBuilderFactory.get("delimitedReaderJob")
                .start(delimitedReaderStep())
                .build();
    }

    @Bean
    public Step delimitedReaderStep(){
        return stepBuilderFactory.get("delimitedReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(delimitedItemReader(null))
                .writer(delimitedItemWriter())
                .build();
    }

    /**
     * DelimitedLineTokenizer
     * 구분자를 통해서 구분, 기본값은 쉼표
     * 인용문자로 값을 구성하는 항목
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> delimitedItemReader(@Value("#{jobParameters['customerFile']}")Resource inputFile){
        return new FlatFileItemReaderBuilder<Customer>()
                .name("delimitedItemReader")
                .resource(inputFile)
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "addressNumber",
                        "street",
                        "city",
                        "state",
                        "zipCode"})
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public ItemWriter<Customer> delimitedItemWriter(){
        return items -> items.forEach(System.out::println);
    }
}
