package com.example.springbatchjoblauncher.transaction;

import com.example.springbatchjoblauncher.transaction.account_step.ApplyTransactionStepConfig;
import com.example.springbatchjoblauncher.transaction.generate_account_step.GenerateAccountSummaryStepConfig;
import com.example.springbatchjoblauncher.transaction.transaction_step.ImportTransactionFileStepConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TransactionJobConfig {
    private final JobBuilderFactory jobBuilderFactory;

    private final ImportTransactionFileStepConfig importTransactionFileStepConfig;
    private final ApplyTransactionStepConfig applyTransactionStepConfig;
    private final GenerateAccountSummaryStepConfig generateAccountSummaryStepConfig;

    @Bean
    public Job transactionJob(){
        return jobBuilderFactory.get("transactionJob")
                .start(importTransactionFileStepConfig.importTransactionFileStep())
                .on("STOPPED").stopAndRestart(importTransactionFileStepConfig.importTransactionFileStep())
                .from(importTransactionFileStepConfig.importTransactionFileStep()).on("*").to(applyTransactionStepConfig.applyTransactionStep())
                .from(applyTransactionStepConfig.applyTransactionStep()).next(generateAccountSummaryStepConfig.generateAccountSummaryStep())
                .end().build();
    }
}
