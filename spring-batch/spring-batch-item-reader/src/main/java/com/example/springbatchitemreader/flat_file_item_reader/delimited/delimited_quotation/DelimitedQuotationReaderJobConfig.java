package com.example.springbatchitemreader.flat_file_item_reader.delimited.delimited_quotation;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DelimitedQuotationReaderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job delimitedQuotationReaderJob(){
        return jobBuilderFactory.get("delimitedQuotationReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(delimitedQuotationReaderStep())
                .build();
    }

    @Bean
    public Step delimitedQuotationReaderStep(){
        return stepBuilderFactory.get("delimitedQuotationReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(delimitedQuotationItemReader(null))
                .writer(delimitedQuotationItemWriter())
                .build();
    }

    /**
     * DelimitedLineTokenizer
     * 구분자를 통해서 구분, 기본값은 쉼표
     * 인용문자로 값을 구성하는 항목
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> delimitedQuotationItemReader(@Value("#{jobParameters['customerFile']}") Resource inputFile){
        return new FlatFileItemReaderBuilder<Customer>()
                .name("delimitedItemReader")
                .resource(inputFile)
                .delimited()
                .quoteCharacter('#')
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "street",
                        "city",
                        "state",
                        "zipCode"})
                .fieldSetMapper(new DelimitedQuotationFieldSetMapper())
                .build();
    }

    @Bean
    public ItemWriter<Customer> delimitedQuotationItemWriter(){
        return items -> items.forEach(System.out::println);
    }
}
