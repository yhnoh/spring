package com.example.springbatchjob.job_parameters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobParametersValidatorJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job parametersValidatorJob(){
        return jobBuilderFactory.get("parametersValidatorJob")
                .start(startParametersValidatorStep())
                .build();
    }

    @Bean
    public Step startParametersValidatorStep(){
        return stepBuilderFactory
                .get("startParametersValidatorStep").tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
                .build();
    }

}
