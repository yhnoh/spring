package com.example.springbatchjob.step_componentization.job_step;

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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobStepJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step beforeJobStepStep(){
        return stepBuilderFactory.get("beforeJobStepStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("=======> beforeJobStepStep");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job beforeJobStepJob(){
        return jobBuilderFactory.get("beforeJobStepJob")
                .start(beforeJobStepStep())
                .build();
    }

    /**
     * Job을 Step으로 감싸서 다시 다른 Job에서 호출할 수 있다.
     * Job을 실행시키는 것이기 때문에 실제 실행시키는 Job과 실행되는 Job의 JOB_EXECUTION은 서로 다르다.
     *
     * 개별 잡을 만들어 마스터 잡과 함께 묶어 사용할 수 있는 기능은 이점처럼 보이다.
     * 하지만 실행 처리를 제어하는데 매우 큰 제약이 있을 수 있다.
     * 잡 관리 기능은 단일 잡 수준에서 이뤄지기 때문에 개발자가 직접 잡의 실행 처리를 제어하는 코드를 작성해야 할 수 있다. (복잡도 증가)
     *  또한 Spring Batch 가이드라인에서는 "가능한한 단순화하고 복잡한 논리적인 로직을 작성하지 마라."라는 문구가 있다.
     * 해당 기능은 꼭 필요한 상황이 아니라면 피하는 것이 좋다.
     */
    @Bean
    public Step initializeJobStepStep(){
        return stepBuilderFactory.get("initializeJobStepStep")
                .job(beforeJobStepJob())
                .build();
    }

    @Bean
    public Job jobStepJob(){
        return jobBuilderFactory.get("jobStepJob")
                .start(initializeJobStepStep())
                .build();
    }
}
