package com.example.springbatchitemreader.error_handler_item_reader.skip_record;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SkipRecordItemReaderConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public static final int SKIP_LIMIT = 10;

    @Bean
    public Job skipRecordItemReaderJob() {
        return jobBuilderFactory.get("skipRecordItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(skipRecordItemReaderStep())
                .build();
    }

    @Bean
    public SkipRecordItemReaderListener skipRecordItemReaderListener() {
        return new SkipRecordItemReaderListener();
    }

    @Bean
    public Step skipRecordItemReaderStep() {
        return stepBuilderFactory.get("skipRecordItemReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(skipRecordItemReader())
                .writer(skipRecordItemWriter())
                .faultTolerant()
//                .skip(Exception.class)
//                .noSkip(RuntimeException.class)
//                .skipLimit(SKIP_LIMIT)
                .skipPolicy(new SkipRecordPolicy())
                .listener(skipRecordItemReaderListener())
                .build();
    }

    @Bean
    public ItemWriter<Customer> skipRecordItemWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public ItemReader<Customer> skipRecordItemReader() {
        SkipRecordItemReader skipRecordItemReader = new SkipRecordItemReader();
        skipRecordItemReader.setName("skipRecordItemReader");
        return skipRecordItemReader;
    }

}
