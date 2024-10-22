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
import org.springframework.batch.item.support.SynchronizedItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = "multiThreadStepsJob")
@RequiredArgsConstructor
@Slf4j
public class MultiThreadStepsJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public TaskExecutor getThreadPoolTaskExecutor(){
        return threadPoolTaskExecutor;
    }

    @Bean
    public Job parallelStepsJob(){
        return new JobBuilder("multiThreadStepsJob", jobRepository)
                .start(this.step1())
                .next(this.step2())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Integer, Integer>chunk(1, platformTransactionManager)
                .reader(this.itemReader1())
                .writer(this.itemWriter1())
                .taskExecutor(this.getThreadPoolTaskExecutor())
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader1(){
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 0)
                .boxed()
                .collect(Collectors.toList());

        ListItemReader<Integer> listItemReader = new ListItemReader<>(list);
        return new SynchronizedItemReader<>(listItemReader);
    }

    @Bean
    public ItemWriter<Integer> itemWriter1(){
        return items -> items.forEach(item -> log.info("step1 item = {}", item));
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .<Integer, Integer>chunk(1, platformTransactionManager)
                .reader(this.itemReader2())
                .writer(this.itemWriter2())
                .taskExecutor(this.getThreadPoolTaskExecutor())
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader2(){
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 1)
                .boxed()
                .collect(Collectors.toList());

        ListItemReader<Integer> listItemReader = new ListItemReader<>(list);
        return new SynchronizedItemReader<>(listItemReader);
    }

    @Bean
    public ItemWriter<Integer> itemWriter2(){
        return items -> items.forEach(item -> log.info("step2 item = {}", item));
    }

}
