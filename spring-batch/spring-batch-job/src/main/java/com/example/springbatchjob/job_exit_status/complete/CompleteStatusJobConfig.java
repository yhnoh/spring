package com.example.springbatchjob.job_exit_status.complete;

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
public class CompleteStatusJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * COMPLETE
     * 성공적으로 종료했음을 의미한다.
     * 동일한 파라미터를 사용해 다시 실행할 수 없다.
     * end 메서드를 활용해 step이 실패하여도 COMPLETE 상태로 만들 수 있다.
     */
    @Bean
    public Job completeStatusJob(){
        return jobBuilderFactory.get("completeStatusJob")
                .start(completeStatusStep())
                .on("FAILED").end()
                .from(completeStatusStep()).on("*").to(completeStatusNextStep()).end()
                .build();
    }

    @Bean
    public Step completeStatusNextStep() {
        return stepBuilderFactory.get("completeStatusNextStep")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED).build();
    }

    @Bean
    public Step completeStatusStep() {
        return stepBuilderFactory.get("completeStatusStep")
                .tasklet((contribution, chunkContext) -> {throw new RuntimeException();}).build();
    }
}
