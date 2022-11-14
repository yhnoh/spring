package com.example.springbatchjob.step_flow.execution_decider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RandomExecutionDeciderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * 현재 스텝을 실행하고 난뒤 특정 스텝을 실행하지 않게 하는 장치로써 JobExecutionDecider를 제공한다.
     * 다음에 무엇을 수행할지에 대한 적절한 결정을 내릴 때 사용할 수 있다.
     */
    @Bean
    public Job randomExecutionDeciderJob(){
        return jobBuilderFactory.get("randomExecutionDeciderJob")
                .start(randomExecutionDeciderStartStep())
                .next(randomExecutionDecider())
                .from(randomExecutionDecider()).on("FAILED").to(randomExecutionDeciderFailureStep())
                .from(randomExecutionDecider()).on("*").to(randomExecutionDeciderSuccessStep())
                .end()
                .build();
    }

    @Bean
    public Step randomExecutionDeciderSuccessStep() {
        return stepBuilderFactory.get("randomExecutionDeciderSuccessStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("randomExecutionDeciderSuccessStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step randomExecutionDeciderFailureStep() {
        return stepBuilderFactory.get("randomExecutionDeciderFailureStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("randomExecutionDeciderFailureStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step randomExecutionDeciderStartStep(){
        return stepBuilderFactory.get("randomExecutionDeciderStartStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("randomExecutionDeciderStartStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public JobExecutionDecider randomExecutionDecider(){
        return new RandomExecutionDecider();
    }
}
