package com.example.springbatchitemwriter.flat_file_item_writer.delimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class DelimiterFileJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job delimiterFileJob(){
        return jobBuilderFactory.get("delimiterFileJob")
                .incrementer(new RunIdIncrementer())
                .start(delimiterFileStep())
                .build();
    }

    @Bean
    public Step delimiterFileStep(){
        return stepBuilderFactory.get("delimiterFileStep")
                .<Customer, Customer>chunk(10)
                .reader(this.delimiterFileItemReader(null))
                .writer(this.delimiterFileItemWriter(null))
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> delimiterFileItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile){

        return new FlatFileItemReaderBuilder<Customer>()
                .name("delimiterFileItemReader")
                .delimited()
                .names(new String[] {"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .resource(inputFile)
                .build();
    }

    /**
     * <pre>구분자로 구분된 파일 출력</pre>
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> delimiterFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile){
        return new FlatFileItemWriterBuilder<Customer>()
                .name("delimiterFileItemWriter")
                .resource(outputFile)
                .delimited()
                .delimiter(";")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
                .build();
    }
}
