package com.example.springbatchitemreader.database_item_reader.jdbc_paging;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    @StepScope
    public JdbcPagingItemReader<Customer> jdbcPagingItemReader(@Value("#{jobParameters['city']}") String city) throws Exception{

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("city", city);

        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("jdbcPagingItemReader")
                .dataSource(dataSource)
                .queryProvider(jdbcPagingQueryProvider())
                .parameterValues(parameterValues)
                .pageSize(10)
                .rowMapper(new JdbcPagingRowMapper())
                .build();
    }

    @Bean
    public PagingQueryProvider jdbcPagingQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause("select *");
        provider.setFromClause("from customer");
        provider.setWhereClause("where city = :city");
        provider.setSortKey("lastName");
        return provider.getObject();
    }

    @Bean
    public Job jdbcPagingItemReaderJob() throws Exception{
        return jobBuilderFactory.get("jdbcPagingItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbcPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcPagingItemReaderStep() throws Exception{
        return stepBuilderFactory.get("jdbcPagingItemReaderStep")
                .<Customer, Customer>chunk(100)
                .reader(jdbcPagingItemReader(null))
                .writer(jdbcCursorItemWriter())
                .build();
    }

    private ItemWriter<Customer> jdbcCursorItemWriter() {
        return items -> items.forEach(System.out::println);
    }
}
