package org.example.springbatchjoblauncher.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job helloJob1() {
        return jobBuilderFactory.get("helloJob1").start(this.helloStep1()).build();
    }


    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1").tasklet((contribution, chunkContext) -> {
            System.out.println("helloJob1 run!!!");
            return RepeatStatus.FINISHED;
        }).build();
    }


    @Bean
    public Job helloJob2() {
        return jobBuilderFactory.get("helloJob2").start(this.helloStep2()).build();
    }


    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2").tasklet((contribution, chunkContext) -> {
            System.out.println("helloStep2 run!!!");
            return RepeatStatus.FINISHED;
        }).build();
    }

}
