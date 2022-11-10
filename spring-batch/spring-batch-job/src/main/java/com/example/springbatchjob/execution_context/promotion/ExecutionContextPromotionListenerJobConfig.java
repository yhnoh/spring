package com.example.springbatchjob.execution_context.promotion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ExecutionContextPromotionListenerJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job executionContextPromotionListenerJob(){
        return jobBuilderFactory.get("executionContextPromotionListenerJob")
                .start(executionContextPromotionListenerStep1())
                .next(executionContextPromotionListenerStep2())
                .build();
    }

    @Bean
    public Step executionContextPromotionListenerStep1(){
        return stepBuilderFactory.get("executionContextPromotionListenerStep1")
                .tasklet((contribution, chunkContext) -> {
                    String name = chunkContext.getStepContext()
                            .getJobParameters()
                            .get("name").toString();

                    ExecutionContext executionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getExecutionContext();

                    executionContext.put("name", name);
                    log.info("step.name = {}", executionContext.get("name"));
                    return RepeatStatus.FINISHED;
                })
                .listener(promotionListener())
                .build();
    }

    /**
     * StepExecutionContext를 JobExecutionContext로 승격시키기 위한 리스너
     */
    private StepExecutionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"name"});
        return listener;
    }


    @Bean
    public Step executionContextPromotionListenerStep2(){
        return stepBuilderFactory.get("executionContextPromotionListenerStep2")
                .tasklet((contribution, chunkContext) -> {

                    ExecutionContext executionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();
                    //승격되었는지 확인
                    log.info("job.name = {}", executionContext.get("name"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
