package com.example.springbatchitemreader.flat_file_item_reader.delimited_tokenizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DelimitedTokenizerReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job delimitedTokenizerReaderJob(){
        return jobBuilderFactory.get("delimitedTokenizerReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(delimitedTokenizerReaderStep())
                .build();
    }

    @Bean
    public Step delimitedTokenizerReaderStep(){
        return stepBuilderFactory.get("delimitedTokenizerReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(delimitedTokenizerItemReader(null))
                .writer(delimitedTokenizerItemWriter())
                .build();
    }

    /**
     * DelimitedLineTokenizer
     * 구분자를 통해서 구분, 기본값은 쉼표
     * 인용문자로 값을 구성하는 항목
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> delimitedTokenizerItemReader(@Value("#{jobParameters['customerFile']}") Resource inputFile){
        return new FlatFileItemReaderBuilder<Customer>()
                .name("delimitedTokenizerItemReader")
                .lineTokenizer(new DelimitedTokenizer())
                .resource(inputFile)
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public ItemWriter<Customer> delimitedTokenizerItemWriter(){
        return items -> items.forEach(System.out::println);
    }
}
