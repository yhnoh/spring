package com.example.springbatchjob.job_exit_status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StopStatusJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * STOPPED
     * STOPPED 상태로 잡이 종료됐다면 잡을 다시 시작할 수 있다.
     * 만약 잡을 다시 시작한다면 첫 스텝이 아닌 사용자가 미리 구성한 스텝에서 다시 시작할 수 있다.
     * 스텝 사이에 사람의 개입이 필요한 경우, 다른 검사나 후처리가 필요한 경우
     * 1. stopStatusStep 실행 시 오류 발생, FAILED 상태일 때 STOPPED 상태로 만듬
     * 2. 재실행 시 stopStatusStep 실행이 아닌 stopStatusRestartStep 실행
     */
    @Bean
    public Job stopStatusJob(){
        return jobBuilderFactory.get("stopStatusJob")
                .start(stopStatusStep())
                .on("FAILED").stopAndRestart(stopStatusRestartStep())
                .from(stopStatusRestartStep()).on("*").to(stopStatusNextStep()).end()
                .build();
    }

    @Bean
    public Step stopStatusRestartStep(){
        return stepBuilderFactory.get("stopStatusRestartStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("========> stopStatusRestartStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step stopStatusNextStep() {
        return stepBuilderFactory.get("stopStatusNextStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("========> stopStatusNextStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step stopStatusStep() {
        return stepBuilderFactory.get("stopStatusStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("========> stopStatusStep");
//                    throw new RuntimeException();
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
