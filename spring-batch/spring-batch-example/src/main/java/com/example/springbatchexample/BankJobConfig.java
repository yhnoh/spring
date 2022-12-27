package com.example.springbatchexample;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor

public class BankJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final Step customerUpdateStep;
    private final Step transactionStep;
    private final Step accountUpdateStep;
    @Bean
    public Job bankJob() throws Exception {
        return jobBuilderFactory.get("bankJob")
                .start(customerUpdateStep)
                .next(transactionStep)
                .next(accountUpdateStep)
                .build();
    }

}
