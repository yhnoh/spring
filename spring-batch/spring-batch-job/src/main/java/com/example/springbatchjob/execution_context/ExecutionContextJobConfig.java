package com.example.springbatchjob.execution_context;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ExecutionContextJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job executionContextJob(){
        return jobBuilderFactory.get("executionContextJob")
                .start(jobExecutionContextStep())
                .next(stepExecutionContextStep())
                .next(stepExecutionContextStep2())
                .build();
    }

    @Bean
    public Step jobExecutionContextStep(){
        return stepBuilderFactory.get("executionContextStep")
                .tasklet((contribution, chunkContext) -> {
                    String name = chunkContext.getStepContext()
                            .getJobParameters()
                            .get("name").toString();

        /*
                    ExecutionContext의 현재 상태를 나타내는 getJobExecutionContext를 편리하게 사용할 수 있지만 제약 조건이 있다.
                    반환한 Map을 변경하더라도 실제 ExecutionContext의 내용이 변하지 않는다.
                    실제로 ExecutionContext의 내용이 변하지 않았기 때문에 오류가 발생하면 사라진다.
                    Map<String, Object> jobExecutionContext = chunkContext.getStepContext()
                            .getJobExecutionContext();
        */
                    //Job의 executionContext
                    ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    jobExecutionContext.put("name", name);
                    log.info("job.name = {}", jobExecutionContext.get("name"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepExecutionContextStep(){
        return stepBuilderFactory.get("stepExecutionContextStep")
                .tasklet((contribution, chunkContext) -> {
                    String name = chunkContext.getStepContext()
                            .getJobParameters()
                            .get("name").toString();

                    ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    ExecutionContext stepExecutionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getExecutionContext();
                    //stepExecutionContext의 범위 확인을 위한 put
                    stepExecutionContext.put("name", name);

                    log.info("job.name = {}", jobExecutionContext.get("name"));
                    log.info("step.name = {}", stepExecutionContext.get("name"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepExecutionContextStep2(){
        return stepBuilderFactory.get("stepExecutionContextStep2")
                .tasklet((contribution, chunkContext) -> {

                    ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    ExecutionContext stepExecutionContext = chunkContext.getStepContext()
                            .getStepExecution()
                            .getExecutionContext();
                    //stepExecutionContext의 범위 확인을 위한 get

                    log.info("job.name = {}", jobExecutionContext.get("name"));
                    log.info("step.name = {}", stepExecutionContext.get("name"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
