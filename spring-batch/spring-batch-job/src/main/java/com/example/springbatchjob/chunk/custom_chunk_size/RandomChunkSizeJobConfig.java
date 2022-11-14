package com.example.springbatchjob.chunk.custom_chunk_size;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RandomChunkSizeJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job randomChunkSizeJob(){
        return jobBuilderFactory.get("randomChunkSizeJob")
                .start(randomChunkSizeStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step randomChunkSizeStep(){
        return stepBuilderFactory.get("randomChunkSizeStep")
                .<String, String>chunk(randomCompletionPolicy())
                .reader(randomChunkSizeItemReader())
                .writer(randomChunkSizeItemWriter())
                .build();

    }


    @Bean
    public ItemReader<String> randomChunkSizeItemReader() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            list.add("item" + i);
        }
        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<? super String> randomChunkSizeItemWriter() {

        return items -> {
            for (String item : items) {
                log.info("items size = {}, item => {}", items.size(), item);
            }
        };
    }
    @Bean
    public CompletionPolicy randomCompletionPolicy() {
        return new RandomCompletionPolicy();
    }
}
