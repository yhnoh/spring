package com.example.springbatchjob.step_componentization.flow_builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlowBuilderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job flowBuilderJob(){
        return jobBuilderFactory.get("flowBuilderJob")
                .start(flowBuilderFlow()).end()
                .build();
    }

    /**
     * FlowBuilder를 활용하여 플로우의 스텝이 잡의 일부분으로 저장된다.
     * 플로우를 사용하는 것과 잡 내에서 스텝을 직접 구성하는 것에 차이가 없다.
     *  STEP_EXECUTION에 flowBuilderFlow는 저장되지 않는다.
     */
    @Bean
    public Flow flowBuilderFlow(){
        return new FlowBuilder<Flow>("flowBuilderFlow").start(flowBuilderStep())
                .next(flowBuilderStepNextStep())
                .build();
    }
    @Bean
    public Step flowBuilderStepNextStep() {
        return stepBuilderFactory.get("flowBuilderStepNextStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("=======> flowBuilderStepNextStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step flowBuilderStep() {
        return stepBuilderFactory.get("flowBuilderStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("=======> flowBuilderStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }


}
