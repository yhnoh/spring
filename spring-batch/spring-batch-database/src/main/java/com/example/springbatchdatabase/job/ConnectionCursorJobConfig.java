package com.example.springbatchdatabase.job;

import com.example.springbatchdatabase.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ConnectionCursorJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    @Bean
    public Job connectionCursorJob() {
        return jobBuilderFactory.get("connectionCursorJob")
                .start(this.connectionCursorStep())
                .build();
    }

    @Bean
    public Step connectionCursorStep() {
        return stepBuilderFactory.get("connectionCursorStep")
                .<Member, Member>chunk(5)
                .reader(this.databaseConnectionItemReader())
                .writer(this.jdbcCursorItemWriter())
                .build();
    }

//    @Bean
//    public DatabaseConnectionItemReader<Member> databaseConnectionItemReader() {
//        JdbcCursorItemReader<Member> jdbcCursorItemReader = new JdbcCursorItemReaderBuilder<Member>()
//                .name("databaseConnectionItemReader")
//                .dataSource(dataSource)
//                .sql("select * from member")
//                .beanRowMapper(Member.class)
//                .build();
//        return new DatabaseConnectionItemReader<>(jdbcCursorItemReader);
//    }

    @Bean
    public DatabaseConnectionItemReader<Member> databaseConnectionItemReader() {
        JdbcPagingItemReader<Member> jdbcPagingItemReader = new JdbcPagingItemReaderBuilder<Member>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .selectClause("select *")
                .fromClause("from Member")
                .beanRowMapper(Member.class)
                .sortKeys(Map.of("id", Order.ASCENDING))
                .pageSize(10)
                .fetchSize(10)
                .build();
        return new DatabaseConnectionItemReader<>(jdbcPagingItemReader);
    }


    @Bean
    public ItemWriter<Member> jdbcCursorItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
