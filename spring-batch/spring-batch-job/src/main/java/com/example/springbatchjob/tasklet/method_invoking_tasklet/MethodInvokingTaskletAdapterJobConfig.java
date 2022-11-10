package com.example.springbatchjob.tasklet.method_invoking_tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MethodInvokingTaskletAdapterJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MethodInvokingTaskletTargetService methodInvokingTaskletTargetService;

    @Bean
    public Job methodInvokingTaskletAdapterJob(){
        return jobBuilderFactory.get("methodInvokingTaskletAdapterJob")
                .start(methodInvokingTaskletAdapterStep())
                .build();
    }

    @Bean
    public Step methodInvokingTaskletAdapterStep(){
        return stepBuilderFactory.get("methodInvokingTaskletAdapterStep")
                .tasklet(methodInvokingTasklet())
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTasklet(){
        MethodInvokingTaskletAdapter methodInvokingTasklet = new MethodInvokingTaskletAdapter();
        methodInvokingTasklet.setTargetObject(methodInvokingTaskletTargetService);
        methodInvokingTasklet.setTargetMethod("targetTasklet");
        return methodInvokingTasklet;
    }
}
