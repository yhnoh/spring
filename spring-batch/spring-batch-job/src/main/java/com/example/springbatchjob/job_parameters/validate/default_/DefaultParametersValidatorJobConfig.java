package com.example.springbatchjob.job_parameters.validate.default_;

import com.example.springbatchjob.job_parameters.validate.custom.FileNameJobParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DefaultParametersValidatorJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job defaultParametersValidatorJob(){
        return jobBuilderFactory.get("defaultParametersValidatorJob")
                .validator(validator())
                .start(defaultParametersValidatorStep(null, null))
                .build();
    }

    /**
     * 스프링 배치에서 제공하는 DefaultJobParametersValidator
     * 필수 파라미터가 누락없이 전달됐는지 확인하는 유효성을 검증할 수 있다.
     * 필수 키에 대한 유효성 체크만 하기 때문에 복잡한 유효성 검증을 위해서는 직접 구현해야 한다.
     */
    public JobParametersValidator validator(){
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
        validator.setRequiredKeys(new String[]{"fileName"});
        validator.setOptionalKeys(new String[]{"name"});
        return validator;
    }
    @StepScope
    @Bean
    public Step defaultParametersValidatorStep(@Value("#{jobParameters[fileName]}") String fileName,
                                               @Value("#{jobParameters[name]}") String name){
        return stepBuilderFactory
                .get("defaultParametersValidatorStep").tasklet((contribution, chunkContext) -> {
                    log.info("fileName = {}, name = {}", fileName, name);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
