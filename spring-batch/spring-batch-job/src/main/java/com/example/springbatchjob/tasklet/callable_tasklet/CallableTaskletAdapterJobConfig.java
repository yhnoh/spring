package com.example.springbatchjob.tasklet.callable_tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Callable;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CallableTaskletAdapterJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job callableTaskletAdapterJob(){
        return jobBuilderFactory.get("callableTaskletAdapterJob")
                .start(callableTaskletAdapterStep())
                .build();
    }

    @Bean
    public Step callableTaskletAdapterStep(){
        return stepBuilderFactory.get("callableTaskletAdapterStep")
                .tasklet(callableTasklet())
                .build();
    }

    @Bean
    public CallableTaskletAdapter callableTasklet(){
        CallableTaskletAdapter callableTaskletAdapter = new CallableTaskletAdapter();
        callableTaskletAdapter.setCallable(() -> {
            log.info("callableTasklet running");
            return RepeatStatus.FINISHED;
        });
        return callableTaskletAdapter;
    }
}
