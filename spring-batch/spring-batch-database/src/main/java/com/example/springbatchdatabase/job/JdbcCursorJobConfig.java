package com.example.springbatchdatabase.job;

import com.example.springbatchdatabase.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcCursorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    @Bean
    public Job jdbcCursorJob() {
        return jobBuilderFactory.get("jdbcCursorJob")
                .start(this.jdbcCursorStep())
                .build();
    }

    @Bean
    public Step jdbcCursorStep() {
        return stepBuilderFactory.get("jdbcCursorStep")
                .<Member, Member>chunk(10)
                .reader(this.jdbcCursorItemReader())
                .writer(this.jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Member> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Member>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("select * from member")
                .beanRowMapper(Member.class)
                .build();
    }

    @Bean
    public ItemWriter<Member> jdbcCursorItemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
