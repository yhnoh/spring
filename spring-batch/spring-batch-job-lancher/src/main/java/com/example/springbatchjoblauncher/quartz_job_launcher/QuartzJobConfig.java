package com.example.springbatchjoblauncher.quartz_job_launcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job quartzJob(){
        return jobBuilderFactory.get("quartzJob")
                .incrementer(new RunIdIncrementer())
                .start(quartzStep())
                .build();
    }

    private Step quartzStep() {
        return stepBuilderFactory.get("quartzStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("=====> quartzStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
