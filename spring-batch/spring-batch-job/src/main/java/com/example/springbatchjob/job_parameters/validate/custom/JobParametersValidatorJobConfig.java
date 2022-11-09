package com.example.springbatchjob.job_parameters.validate.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobParametersValidatorJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JobParameter에 대한 유효성 검증기를 직접 커스터마이징할 수 있다.
     */
    @Bean
    public Job jobParametersValidatorJob(){
        return jobBuilderFactory.get("jobParametersValidatorJob")
                .validator(new FileNameJobParameterValidator())
                .start(jobParametersValidatorStep())
                .build();
    }
    @Bean
    public Step jobParametersValidatorStep(){
        return stepBuilderFactory
                .get("jobParametersValidatorStep").tasklet(jobParameterValidatorTasklet(null))
                .build();
    }

    @StepScope
    @Bean
    public Tasklet jobParameterValidatorTasklet(@Value("#{jobParameters[fileName]}") String fileName) {
        return (contribution, chunkContext) -> {
            log.info("fileName = {}", fileName);
            return RepeatStatus.FINISHED;
        };
    }

}
