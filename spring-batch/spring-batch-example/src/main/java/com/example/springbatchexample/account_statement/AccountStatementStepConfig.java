package com.example.springbatchexample.account_statement;

import com.example.springbatchexample.account_statement.dto.Customer;
import com.example.springbatchexample.account_statement.dto.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class AccountStatementStepConfig {
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step accountStatementStep(AccountStatementItemProcessor accountStatementItemProcessor){
        return stepBuilderFactory.get("accountStatementStep")
                .<Statement, Statement>chunk(1)
                .reader(this.accountStatementItemReader(null))
                .processor(accountStatementItemProcessor)
                .writer(this.accountStatementItemWriter(null))
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Statement> accountStatementItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Statement>()
                .name("accountStatementItemReader")
                .dataSource(dataSource)
                .sql("select customer_id, " +
                        "first_name, " +
                        "middle_name, " +
                        "last_name, " +
                        "address1, " +
                        "address2, " +
                        "city, " +
                        "state, " +
                        "postal_code, " +
                        "ssn, " +
                        "email_address, " +
                        "home_phone, " +
                        "cell_phone, " +
                        "work_phone, " +
                        "notification_pref " +
                        "from customer")
                .rowMapper((rs, rowNum) -> {
                    Customer customer = Customer.builder()
                            .id(rs.getLong("customer_id"))
                            .firstName(rs.getString("first_name"))
                            .middleName(rs.getString("middle_name"))
                            .lastName(rs.getString("last_name"))
                            .address1(rs.getString("address1"))
                            .address2(rs.getString("address2"))
                            .city(rs.getString("city"))
                            .state(rs.getString("state"))
                            .postalCode(rs.getString("postal_code"))
                            .ssn(rs.getString("ssn"))
                            .emailAddress(rs.getString("email_address"))
                            .homePhone(rs.getString("home_phone"))
                            .cellPhone(rs.getString("cell_phone"))
                            .workPhone(rs.getString("work_phone"))
                            .notificationPreferences(rs.getInt("notification_pref"))
                            .build();

                    return new Statement(customer);
                }).build();
    }

    @Bean
    @StepScope
    public MultiResourceItemWriter<Statement> accountStatementItemWriter(@Value("#{jobParameters['outputDirectory']}")FileSystemResource resource) {
        return new MultiResourceItemWriterBuilder<Statement>()
                .name("accountStatementItemWriter")
                .resource(resource)
                .itemCountLimitPerResource(1)
                .delegate(this.accountStatementFileItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemWriter<Statement> accountStatementFileItemWriter() {
        FlatFileItemWriter<Statement> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setName("accountStatementFileItemWriter");
        itemWriter.setHeaderCallback(new StatementHeaderCallback());
        itemWriter.setLineAggregator(new StatementLineAggregator());
        return itemWriter;
    }
}
