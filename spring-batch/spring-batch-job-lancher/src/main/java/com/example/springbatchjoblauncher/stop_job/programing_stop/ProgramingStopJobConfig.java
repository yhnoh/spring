package com.example.springbatchjoblauncher.stop_job.programing_stop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProgramingStopJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job programingStopJob(){
        return jobBuilderFactory.get("programingStopJob")
                .listener(this.programingStopStep())
                .start(this.programingStopStep())
                .on("STOPPED").stopAndRestart(this.programingStopStep())
                .from(this.programingStopStep()).on("*").to(this.programingStopNextStep())
                .end().build();
    }

    @Bean
    @StepScope
    public ProgramingStopItemReader programingStopItemReader(@Value("#{jobParameters['expectedRecodeCount']}") Integer expectedRecodeCount){
        return new ProgramingStopItemReader(this.programingStopItemFileReader(null), expectedRecodeCount);
    }

    @Bean
    @StepScope
    public ItemStreamReader<FieldSet> programingStopItemFileReader(@Value("#{jobParameters['transactionFile']}") ClassPathResource resource) {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("fileItemReader")
                .resource(resource)
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .build();
    }

    @Bean
    public Step programingStopStep(){
        return this.stepBuilderFactory.get("programingStopStep")
                .<ProgramingTransaction, ProgramingTransaction>chunk(100)
                .reader(this.programingStopItemReader(null))
                .writer(this.programingStopItemWriter())
                .allowStartIfComplete(true)
                .listener(this.programingStopItemReader(null))
                .build();
    }

    @Bean
    public ItemWriter<ProgramingTransaction> programingStopItemWriter(){
        return transactions -> transactions.forEach(transaction -> log.info("size = {} transaction = {} ", transactions.size(),transaction));
    }

    @Bean
    public Step programingStopNextStep(){
        return this.stepBuilderFactory.get("programingStopNextStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("programingStopNextStep FINISHED!!!!!!!!!!!!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
