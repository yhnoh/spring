package com.example.springbatchjob.chunk.chunk_size;

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
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ChunkSizeJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * chunkSize는 정적인 커밋개수 설정 방버ㅂ과 CompletionPoliy 구현체를 사용하여 설정할 수 있다.
     */

    @Bean
    public Job chunkSizeJob(){
        return jobBuilderFactory.get("chunkSizeJob")
                .start(chunkSizeStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step chunkSizeStep(){
        return stepBuilderFactory.get("chunkSizeStep")
                .<String, String>chunk(chunkSizeCompletionPolicy())
                .reader(chunkSizeItemReader())
                .writer(chunkSizeItemWriter())
                .build();

    }


    @Bean
    public ItemReader<String> chunkSizeItemReader() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            list.add("item" + i);
        }
        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<? super String> chunkSizeItemWriter() {

        return items -> {
            for (String item : items) {
                log.info("items size = {}, item => {}", items.size(), item);
            }
        };
    }
    @Bean
    public CompletionPolicy chunkSizeCompletionPolicy() {

        CompositeCompletionPolicy policy = new CompositeCompletionPolicy();
        policy.setPolicies(new CompletionPolicy[]{
                new TimeoutTerminationPolicy(3),
                new SimpleCompletionPolicy(1000)
        });
        return policy;
    }
}
