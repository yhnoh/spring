package com.example.springbatchjob.chunk.chunk_based;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ChunkBasedJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkBasedJob() {
        return jobBuilderFactory.get("chunkBasedJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkBasedStep())
                .build();
    }

    /**
     * 10은 10개 단위로 레코드를 처리한 후 작업이 커밋된다는 것을 의미하다.
     */
    @Bean
    public Step chunkBasedStep() {
        return stepBuilderFactory.get("chunkBasedStep")
                .<String, String>chunk(10)
                .reader(chunkBasedItemReader())
                .writer(chunkBasedItemWriter())
                .build();
    }

    @Bean
    public ItemReader<String> chunkBasedItemReader() {

        List<String> items = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            items.add("Item" + i);
        }

        return new ListItemReader<>(items);
    }

    @Bean
    public ItemWriter<String> chunkBasedItemWriter() {
        return items -> {
            for (String item : items) {
                log.info("items size = {}, item => {}", items.size(), item);
            }
        };
    }


}
