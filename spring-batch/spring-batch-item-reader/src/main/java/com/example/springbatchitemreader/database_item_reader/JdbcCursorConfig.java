package com.example.springbatchitemreader.database_item_reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class JdbcCursorConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jdbcCursorJob(){
        return jobBuilderFactory.get("jdbcCursorJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbcCursorStep())
                .build();
    }

    @Bean
    public Step jdbcCursorStep(){
        return stepBuilderFactory.get("jdbcCursorStep")
                .<Customer, Customer>chunk(100)
                .reader(jdbcCursorItemReader(null))
                .writer(jdbcCursorItemWriter())
                .build();
    }

    private ItemWriter<Customer> jdbcCursorItemWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public JdbcCursorItemReader<Customer> jdbcCursorItemReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("select * from customer where city = ?")
                .rowMapper(new JdbcCursorRowMapper())
                .preparedStatementSetter(jdbcCursorParameterSetter(null))
                .build();
    }

    @Bean
    @StepScope
    public PreparedStatementSetter jdbcCursorParameterSetter(@Value("#{jobParameters['city']}") String city) {
        return new ArgumentPreparedStatementSetter(new Object[] {city});
    }


}

