package com.example.springbatchdatabase;

import com.example.springbatchdatabase.entity.Member;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class HibernateCursorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job hibernateCursorJob() {
        return jobBuilderFactory.get("hibernateCursorJob")
                .start(this.hibernateCursorStep())
                .build();
    }

    @Bean
    public Step hibernateCursorStep() {
        return stepBuilderFactory.get("hibernateCursorStep")
                .<Member, Member>chunk(20)
                .reader(this.hibernateCursorItemReader())
                .processor(this.hibernateCursorItemProcessor())
                .writer(this.hibernateCursorItemWriter())
                .build();
    }

    @Bean
    public HibernateCursorItemReader<Member> hibernateCursorItemReader() {
        return new HibernateCursorItemReaderBuilder<Member>()
                .name("hibernateCursorItemReader")
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .queryString("from Member")
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> hibernateCursorItemProcessor() {
        return item -> {
            String changeUsername = item.getUsername().replace("username", "un");
            item.changeUsername(changeUsername);
            return item;
        };
    }

    @Bean
    public JpaItemWriter<Member> hibernateCursorItemWriter() {
        return new JpaItemWriterBuilder<Member>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
