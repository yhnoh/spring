package com.example.springbatchtest;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import javax.batch.api.chunk.ItemReader;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
@RequiredArgsConstructor
public class DataJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job dataJob(){
        return jobBuilderFactory.get("dataJob")
                .start(this.dataStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step dataStep(){
        return stepBuilderFactory.get("dataStep")
                .<DailyStockPrice, DailyStockPriceEntity>chunk(10)
                .reader(this.dataItemReader())
                .processor(this.dataItemProcessor())
                .writer(this.dataItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<DailyStockPrice> dataItemReader(){
        Resource resource = new ClassPathResource("./data/data.csv");

        return new FlatFileItemReaderBuilder<DailyStockPrice>()
                .name("dataItemReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("marketDate", "openPrice", "highPrice", "lowPrice", "closePrice", "volume", "isOpen")
                .fieldSetMapper(new DailyStockPriceFieldSetMapper())
                .build();
    }

    @RequiredArgsConstructor
    public static class DailyStockPriceFieldSetMapper implements FieldSetMapper<DailyStockPrice> {
        @Override
        public DailyStockPrice mapFieldSet(FieldSet fieldSet) throws BindException {
            return DailyStockPrice.builder()
                    .marketDate(fieldSet.readDate("marketDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .openPrice(BigDecimal.valueOf(fieldSet.readDouble("openPrice")))
                    .highPrice(BigDecimal.valueOf(fieldSet.readDouble("highPrice")))
                    .closePrice(BigDecimal.valueOf(fieldSet.readDouble("closePrice")))
                    .lowPrice(BigDecimal.valueOf(fieldSet.readDouble("lowPrice")))
                    .volume(fieldSet.readLong("volume"))
                    .build();
        }
    }

    @Bean
    public ItemProcessor<DailyStockPrice, DailyStockPriceEntity> dataItemProcessor(){
        return item -> DailyStockPriceEntity.builder()
                .marketDate(item.getMarketDate())
                .openPrice(item.getOpenPrice())
                .highPrice(item.getHighPrice())
                .closePrice(item.getClosePrice())
                .lowPrice(item.getLowPrice())
                .volume(item.getVolume())
                .build();
    }
    @Bean
    public JpaItemWriter<DailyStockPriceEntity> dataItemWriter(EntityManagerFactory entityManagerFactory){
        return new JpaItemWriterBuilder<DailyStockPriceEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
