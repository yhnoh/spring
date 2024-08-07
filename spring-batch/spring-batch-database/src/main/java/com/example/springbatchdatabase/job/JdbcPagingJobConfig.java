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
public class JdbcPagingJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    @Bean
    public Job jdbcPagingJob() {
        return jobBuilderFactory.get("jdbcPagingJob")
                .start(this.jdbcPagingStep())
                .build();
    }

    @Bean
    public Step jdbcPagingStep() {
        return stepBuilderFactory.get("jdbcPagingStep")
                .<Member, Member>chunk(10)
                .reader(this.jdbcPagingItemReader())
                .writer(this.jdbcPagingItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Member> jdbcPagingItemReader() {
        return new JdbcPagingItemReaderBuilder<Member>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .selectClause("select *")
                .fromClause("from Member")
                .beanRowMapper(Member.class)
                .sortKeys(Map.of("id", Order.ASCENDING))
                .pageSize(10)
                .fetchSize(10)
                .build();
    }

    @Bean
    public ItemWriter<Member> jdbcPagingItemWriter() {
        return items -> items.forEach(System.out::println);
    }


}
