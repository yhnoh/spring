package com.example.springbatchjob.job_parameters.incrementer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobParametersIncrementerJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JobParameterIncrementer
     * 잡에서 사용할 JobParameter를 고유하게 생성할 수 있도록 도와준다.
     * 사용자가 커스텀도 가능하다.
     */
    @Bean
    public Job jobParametersIncrementerJob(){
        return jobBuilderFactory.get("jobParametersIncrementerJob")
                .incrementer(new RunIdIncrementer())
                .start(JobParametersIncrementerStep())
                .build();
    }

    @Bean
    public Step JobParametersIncrementerStep(){
        return stepBuilderFactory
                .get("JobParametersIncrementerStep").tasklet((contribution, chunkContext) -> {
                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                    log.info("run.id = {}", jobParameters.get("run.id"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
