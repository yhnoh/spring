package org.example.springbatchmulti.step;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = "parallelStepsJob")
@RequiredArgsConstructor
@Slf4j
public class ParallelStepsJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public TaskExecutor getThreadPoolTaskExecutor(){
        return threadPoolTaskExecutor;
    }

    @Bean
    public Job parallelStepsJob(){
        return new JobBuilder("parallelStepsJob", jobRepository)
                .start(this.parallelStepsFlow())
                .end().build();
    }

    @Bean
    public Flow parallelStepsFlow(){
        return new FlowBuilder<SimpleFlow>("parallelStepsFlow")
                .split(this.getThreadPoolTaskExecutor())
                .add(stepFlow1(), stepFlow2())
                .build();
    }

    @Bean
    public Flow stepFlow1() {
        return new FlowBuilder<SimpleFlow>("stepFlow1")
                .start(this.step1())
                .build();
    }

    @Bean
    public Flow stepFlow2() {
        return new FlowBuilder<SimpleFlow>("stepFlow1")
                .start(this.step2())
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
    public ItemReader<Integer> itemReader1(){
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 0)
                .boxed()
                .collect(Collectors.toList());

        return new ListItemReader<>(list);
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
                .build();
    }

    @Bean
    public ItemReader<Integer> itemReader2(){
        List<Integer> list = IntStream.range(1, 101)
                .filter(i -> i % 2 == 1)
                .boxed()
                .collect(Collectors.toList());

        return new ListItemReader<>(list);
    }

    @Bean
    public ItemWriter<Integer> itemWriter2(){
        return items -> items.forEach(item -> log.info("step2 item = {}", item));
    }



}