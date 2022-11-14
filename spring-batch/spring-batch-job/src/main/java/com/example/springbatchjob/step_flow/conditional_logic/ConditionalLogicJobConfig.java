package com.example.springbatchjob.step_flow.conditional_logic;

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

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConditionalLogicJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * on 메서드는 ExitStatus를 통해 어떤일을 수행할지를 결정할 수 있다.
     * *,?와 같은 와일드 카드를 활용 가능하다.
     */
    @Bean
    public Job conditionalLogicJob(){
        return jobBuilderFactory.get("conditionalLogicJob")
                .start(conditionalLogicStartStep())
                .on("FAILED").to(conditionalLogicFailureStep())
                .from(conditionalLogicStartStep()).on("*").to(conditionalLogicSuccessStep())
                .end()
                .build();
    }

    @Bean
    public Step conditionalLogicSuccessStep() {
        return stepBuilderFactory.get("conditionalLogicSuccessStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("conditionalLogicSuccessStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step conditionalLogicFailureStep() {
        return stepBuilderFactory.get("conditionalLogicSuccessStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("conditionalLogicFailureStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step conditionalLogicStartStep(){
        return stepBuilderFactory.get("conditionalLogicSuccessStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("conditionalLogicStartStep");
                    throw new RuntimeException();
//                    return RepeatStatus.FINISHED;
                }).build();
    }
}
