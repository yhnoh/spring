package com.example.springbatchjoblauncher.stop_job.step_execution_stop;

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
public class StepExecutionStopJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepExecutionStopJob(){
        return jobBuilderFactory.get("stepExecutionStopJob")
                .listener(this.stepExecutionStopStep())
                .start(this.stepExecutionStopStep())
                .on("STOPPED").stopAndRestart(this.stepExecutionStopStep())
                .from(this.stepExecutionStopStep()).on("*").to(this.stepExecutionStopNextStep())
                .end().build();
    }

    @Bean
    @StepScope
    public StepExecutionStopItemReader stepExecutionStopItemReader(@Value("#{jobParameters['expectedRecodeCount']}") Integer expectedRecodeCount){
        return new StepExecutionStopItemReader(this.stepExecutionStopItemFileReader(null), expectedRecodeCount);
    }

    @Bean
    @StepScope
    public ItemStreamReader<FieldSet> stepExecutionStopItemFileReader(@Value("#{jobParameters['transactionFile']}") ClassPathResource resource) {
        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("stepExecutionStopItemFileReader")
                .resource(resource)
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .build();
    }

    @Bean
    public Step stepExecutionStopStep(){
        return this.stepBuilderFactory.get("stepExecutionStopStep")
                .<StepExecutionTransaction, StepExecutionTransaction>chunk(100)
                .reader(this.stepExecutionStopItemReader(null))
                .writer(this.stepExecutionStopStepItemWriter())
                .allowStartIfComplete(true)
                .listener(this.stepExecutionStopItemReader(null))
                .build();
    }

    @Bean
    public ItemWriter<StepExecutionTransaction> stepExecutionStopStepItemWriter(){
        return transactions -> transactions.forEach(transaction -> log.info("size = {} transaction = {} ", transactions.size(),transaction));
    }

    @Bean
    public Step stepExecutionStopNextStep(){
        return this.stepBuilderFactory.get("stepExecutionStopNextStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("stepExecutionStopNextStep FINISHED!!!!!!!!!!!!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
