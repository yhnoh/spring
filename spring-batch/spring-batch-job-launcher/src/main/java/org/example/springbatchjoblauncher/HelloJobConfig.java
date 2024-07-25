package org.example.springbatchjoblauncher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class HelloJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job helloJob1() {
        return new JobBuilder("helloJob1", jobRepository)
                .start(this.helloStep1())
                .build();
    }

    @Bean
    public Step helloStep1(){
        return new StepBuilder("helloStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("helloJob1 run!!!");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Job helloJob2() {
        return new JobBuilder("helloJob2", jobRepository)
                .start(this.helloStep2())
                .build();
    }

    @Bean
    public Step helloStep2(){
        return new StepBuilder("helloStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("helloStep2 run!!!");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

}
