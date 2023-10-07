package com.example.springbatchexitcode;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CompletedJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "completedJob")
    public Job completedJob(){
        return jobBuilderFactory.get("completedJob")
                .start(this.completedStep())
                .build();
    }

    @Bean(name = "completedStep")
    public Step completedStep(){
        return stepBuilderFactory.get("completedStep")
                .tasklet(this.complatedTasklet())
                .build();
    }

    @Bean(name = "completedTasklet")
    public Tasklet complatedTasklet(){
        return (contribution, chunkContext) -> RepeatStatus.FINISHED;
    }


}
