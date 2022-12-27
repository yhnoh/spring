package com.example.springbatchexample.transaction;

import com.example.springbatchexample.transaction.dto.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class AccountUpdateStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    @Bean
    public Step accountUpdateStep(){
        return stepBuilderFactory.get("accountUpdateStep")
                .<Transaction, Transaction>chunk(100)
                .reader(this.accountUpdateItemReader(null))
                .writer(this.accountUpdateItemWriter(null))
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Transaction> accountUpdateItemReader(DataSource dataSource) {

        return new JdbcCursorItemReaderBuilder<Transaction>()
                .name("accountUpdateItemReader")
                .dataSource(dataSource)
                .sql("select transaction_id, " +
                        "account_account_id, " +
                        "description, " +
                        "credit, " +
                        "debit, " +
                        "timestamp " +
                        "from transaction " +
                        "order by timestamp")
                .rowMapper((rs, rowNum) -> new Transaction(rs.getLong("transaction_id"),
                        rs.getLong("account_account_id"),
                        rs.getString("description"),
                        rs.getBigDecimal("credit"),
                        rs.getBigDecimal("debit"),
                        rs.getTimestamp("timestamp")
                ))
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> accountUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql("UPDATE ACCOUNT SET " +
                        "BALANCE = BALANCE + :transactionAmount " +
                        "WHERE ACCOUNT_ID = :accountId")
                .beanMapped()
                .assertUpdates(true)
                .build();
    }


}
