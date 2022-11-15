package com.example.springbatchjob.job_exit_status.fail;

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
public class FailStatusJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * FAILED
     * 잡이 성공적으로 완료되지 않음을 의미한다.
     * 동일한 파라미터로 잡을 다시 실행할 수 있다.
     * fail메소드를 활용해 잡의 상태를 FAILED로 만들 수 있다.
     */
    @Bean
    public Job failStatusJob(){
        return jobBuilderFactory.get("failStatusJob")
                .start(failStatusStep())
                .on("FAILED").fail()
                .from(failStatusStep()).on("*").to(failStatusNextStep()).end()
                .build();
    }

    @Bean
    public Step failStatusNextStep() {
        return stepBuilderFactory.get("failStatusNextStep")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED).build();
    }

    @Bean
    public Step failStatusStep() {
        return stepBuilderFactory.get("failStatusStep")
                .tasklet((contribution, chunkContext) -> {throw new RuntimeException();}).build();
    }
}
