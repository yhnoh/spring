package org.example.springbatchitemwriterjpa;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.example.springbatchitemwriterjpa.jpa.member.MemberJpaEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class MemberOrderCountJobConfig {

    public static final int CHUNK_SIZE = 1;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MemberOrderCountItemWriter memberOrderCountItemWriter;

    private final MemberService memberService;

    @Bean
    public Job memberOrderCountJob() {
        return new JobBuilder("memberOrderCountJob", jobRepository).start(this.memberOrderCountStep()).listener(new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {

            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                List<MemberJpaEntity> memberJpaEntities = memberService.findAll();
                for (MemberJpaEntity memberJpaEntity : memberJpaEntities) {
                    System.out.println("memberJpaEntity = " + memberJpaEntity);
                }
            }
        }).build();
    }


    @Bean
    public Step memberOrderCountStep() {
        return new StepBuilder("memberOrderCountStep", jobRepository).<MemberJpaEntity, MemberJpaEntity>chunk(CHUNK_SIZE,
                platformTransactionManager).reader(this.memberOrderCountItemReader(null)).writer(memberOrderCountItemWriter).build();
    }


    @Bean
    public JpaPagingItemReader<MemberJpaEntity> memberOrderCountItemReader(
            @Qualifier("memberEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<MemberJpaEntity>().name("memberOrderCountItemReader")
                .entityManagerFactory(entityManagerFactory).queryString("select m from MemberJpaEntity m").pageSize(CHUNK_SIZE).build();
    }
}
