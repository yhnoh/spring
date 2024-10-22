package org.example.springbatchmulti.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = "simpleStepsJob")
@RequiredArgsConstructor
@Slf4j
public class SimpleStepsJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job simpleStepsJob() {
        return new JobBuilder("simpleStepsJob", jobRepository)
                .start(this.step1())    //짝수를 출력하는 step
                .next(this.step2())     //홀수를 출력하는 step
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Integer, Integer>chunk(1, platformTransactionManager)
                .reader(this.itemReader1())
                .writer(this.itemWriter1())
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader1() {
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 0)
                .boxed()
                .collect(Collectors.toList());

        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<Integer> itemWriter1() {
        return items -> items.forEach(item -> log.info("step1 item = {}", item));
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .<Integer, Integer>chunk(1, platformTransactionManager)
                .reader(this.itemReader2())
                .writer(this.itemWriter2())
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader2() {
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 1)
                .boxed()
                .collect(Collectors.toList());
        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<Integer> itemWriter2() {
        return items -> items.forEach(item -> log.info("step2 item = {}", item));
    }

}
