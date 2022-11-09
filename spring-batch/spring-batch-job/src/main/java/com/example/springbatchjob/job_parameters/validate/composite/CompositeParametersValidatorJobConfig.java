package com.example.springbatchjob.job_parameters.validate.composite;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompositeParametersValidatorJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job compositeParametersValidatorJob(){
        return jobBuilderFactory.get("compositeParametersValidatorJob")
                .validator(validator())
                .start(compositeParametersValidatorStep())
                .build();
    }

    /**
     * 스프링 배치에서 제공하는 CompositeJobParametersValidator
     * 두개 이상의 JobParametersValidator를 함께 사용할 수 있도록 제공해준다.
     */
    public JobParametersValidator validator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultValidator = new DefaultJobParametersValidator();
        defaultValidator.setRequiredKeys(new String[]{"fileName"});

        defaultValidator.afterPropertiesSet();

        NameJobParameterValidator nameValidator = new NameJobParameterValidator();

        validator.setValidators(List.of(defaultValidator, nameValidator));

        return validator;
    }


    @Bean
    public Step compositeParametersValidatorStep(){
        return stepBuilderFactory
                .get("compositeParametersValidatorStep").tasklet(compositeParametersValidatorTasklet(null,null))
                .build();
    }

    @StepScope
    @Bean
    public Tasklet compositeParametersValidatorTasklet(@Value("#{jobParameters[fileName]}") String fileName,
                                                       @Value("#{jobParameters[name]}") String name){
        return (contribution, chunkContext) -> {
            log.info("fileName = {}, name = {}", fileName, name);
            return RepeatStatus.FINISHED;
        };
    }

}
