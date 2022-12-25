package com.example.springbatchitemwriter.flat_file_item_writer.header_footer;

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
public class FileFooterJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final FileFooterCallback fileFooterCallback;

    @Bean
    public Job fileFooterJob() {
        return jobBuilderFactory.get("fileFooterJob")
                .incrementer(new RunIdIncrementer())
                .start(fileFooterStep())
                .build();
    }

    @Bean
    public Step fileFooterStep() {
        return stepBuilderFactory.get("fileFooterStep")
                .<Customer, Customer>chunk(10)
                .reader(this.fileFooterItemReader(null))
                .writer(this.fileFooterItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> fileFooterItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("fileFooterItemReader")
                .delimited()
                .names(new String[]{"firstName",
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

    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> fileFooterItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile) {
        return new FlatFileItemWriterBuilder<Customer>()
                .name("fileFooterItemWriter")
                .resource(outputFile)
                .footerCallback(fileFooterCallback)
                .delimited()
                .delimiter(",")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
                .build();
    }
}
