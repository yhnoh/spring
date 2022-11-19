package com.example.springbatchjoblauncher.stop_job.account_step;

import com.example.springbatchjoblauncher.stop_job.AccountSummary;
import com.example.springbatchjoblauncher.stop_job.repository.TransactionDao;
import com.example.springbatchjoblauncher.stop_job.repository.TransactionDaoSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplyTransactionStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    @Bean
    @StepScope
    public JdbcCursorItemReader<AccountSummary> accountSummaryReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<AccountSummary>()
                .name("accountSummaryReader")
                .dataSource(dataSource)
                .sql("SELECT ACCOUNT_NUMBER, CURRENT_BALANCE " +
                        "FROM ACCOUNT_SUMMARY A " +
                        "WHERE A.ID IN (" +
                        "   SELECT DISTINCT T.ACCOUNT_SUMMARY_ID " +
                        "   FROM TRANSACTION T)" +
                        "ORDER BY A.ACCOUNT_NUMBER")
                .rowMapper(new RowMapper<AccountSummary>() {
                    @Override
                    public AccountSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AccountSummary accountSummary = new AccountSummary();
                        accountSummary.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
                        accountSummary.setCurrentBalance(rs.getDouble("CURRENT_BALANCE"));
                        return accountSummary;
                    }
                }).build();
    }

    @Bean
    public TransactionDao transactionDao(DataSource dataSource){
        return new TransactionDaoSupport(dataSource);
    }

    @Bean
    public TransactionApplierProcessor transactionApplierProcessor(){
        return new TransactionApplierProcessor(transactionDao(null));
    }

    @Bean
    public JdbcBatchItemWriter<AccountSummary> accountSummaryWriter(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<AccountSummary>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("UPDATE ACCOUNT_SUMMARY " +
                        "SET CURRENT_BALANCE = :currentBalance " +
                        "WHERE ACCOUNT_NUMBER = : accountNumber")
                .build();
    }

    @Bean
    public Step applyTransactionStep(){
        return stepBuilderFactory.get("applyTransactionStep")
                .<AccountSummary, AccountSummary>chunk(100)
                .reader(accountSummaryReader(null))
                .processor(transactionApplierProcessor())
                .writer(accountSummaryWriter(null))
                .build();
    }
}
