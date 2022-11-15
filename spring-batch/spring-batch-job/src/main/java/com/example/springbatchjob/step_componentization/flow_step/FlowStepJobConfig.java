package com.example.springbatchjob.step_componentization.flow_step;

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
public class FlowStepJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowStepJob(){
        return jobBuilderFactory.get("flowStepJob")
                .start(flowStep())
                .build();
    }

    /**
     * FlowBuilder를 통해서 생성한 flow를 Step으로 감싸준다. 이렇게 하면 해당 플로우 감긴 스템을 하나의 스텝처럼 기록한다.
     *  STEP_EXECUTION에 기록된다.
     * Flow를 직접 Job에서 실행시키는 것이 아닌 flow를 하나의 Step으로 만들기 때문에 모니터링과 리포팅의 이점이 있다.
     * 실제 코드를 해석할 때도 하나의 job안에 여러 flow가 있다면 생각보다 코드를 이해하기 힘들것이다.
     * 또한 만약 STEP_EXECUTION에 기록이 되지 않느다면 어디서부터 어디까지가 하나의 Flow인지 이해하기 힘들것이다.
     */
    @Bean
    public Step flowStep(){
        return stepBuilderFactory.get("flowStep")
                .flow(flowStepFlow())
                .build();
    }
    @Bean
    public Flow flowStepFlow(){
        return new FlowBuilder<Flow>("flowStepFlow").start(flowStepStartStep())
                .next(flowStepNextStep())
                .build();
    }
    @Bean
    public Step flowStepNextStep() {
        return stepBuilderFactory.get("flowStepNextStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("=======> flowStepNextStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step flowStepStartStep() {
        return stepBuilderFactory.get("flowStepStartStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("=======> flowStepStartStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
