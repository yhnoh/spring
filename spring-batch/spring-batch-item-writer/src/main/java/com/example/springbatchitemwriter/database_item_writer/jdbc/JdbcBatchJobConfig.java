package com.example.springbatchitemwriter.database_item_writer.jdbc;

import com.example.springbatchitemwriter.database_item_writer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class JdbcBatchJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job jdbcBatchJob() {
        return jobBuilderFactory.get("jdbcBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbcBatchStep())
                .build();
    }

    @Bean
    public Step jdbcBatchStep() {
        return stepBuilderFactory.get("jdbcBatchStep")
                .<Customer, Customer>chunk(10)
                .reader(this.jdbcBatchItemReader(null))
                .writer(this.jdbcBatchItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> jdbcBatchItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("jdbcBatchItemReader")
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .resource(inputFile)
                .build();
    }

    /**
     * <pre>JdbcBatchItemWriter > PreparedStatement (물음표) 구문을 사용하여 질의</pre>
     * <p>속도적인 측면에서 다른 퍼시스턴스 프레임워크에 비해 빠르다.</p>
     * <p>데이터 소스, 실행할 SQL, ItemPreparedStatementSetter 인터페이스 구현체 제공</p>
     * <p>ItemPreparedStatementSetter 에는 PreparedStatement API를 사용해 각 값에 채우도록 구현해주면 된다.</p>
     */
//    @Bean
//    public JdbcBatchItemWriter<Customer> jdbcBatchItemWriter() {
//        return new JdbcBatchItemWriterBuilder<Customer>()
//                .dataSource(dataSource)
//                .sql("INSERT INTO CUSTOMER (first_name, " +
//                        "middle_initial, " +
//                        "last_name, " +
//                        "address, " +
//                        "city, " +
//                        "state, " +
//                        "zip) VALUES (?, ?, ?, ?, ?, ?, ?)")
//                .itemPreparedStatementSetter(new JdbcItemPreparedStatementSetter())
//                .build();
//    }

    /**
     * <pre>JdbcBatchItemWriter > 네임드 파라미터 구문을 사용하여 질의</pre>
     * <p>대부분의 스프링 환경에서 파라미터를 채우는것을 더 선호한다. (명확하면서 안전하다.)</p>
     * <p>ItemSqlParameterSourceProvider 구현체를 활용해 아이템에서 파라미터를 추출해 ParameterSource 객체로 반환하는 역할</p>
     * <p>BeanPropertyItemSqlParameterSourceProvider를 사용해 SQL에 채울 값을 아이템에서 추출하는데 사용 -> beanMapped() 메서드를 호출하면 된다.</p>
     */
    @Bean
    public JdbcBatchItemWriter<Customer> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("INSERT INTO CUSTOMER (first_name, " +
                        "middle_initial, " +
                        "last_name, " +
                        "address, " +
                        "city, " +
                        "state, " +
                        "zip) VALUES (:firstName, :middleInitial, :lastName, :address, :city, :state, :zip)")
                .beanMapped()
                .build();
    }
}
