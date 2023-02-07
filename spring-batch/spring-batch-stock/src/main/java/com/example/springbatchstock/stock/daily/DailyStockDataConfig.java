package com.example.springbatchstock.stock.daily;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
@RequiredArgsConstructor
public class DailyStockDataConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job dailyStockDataJob() {
        return jobBuilderFactory.get("dailyStockDataJob")
                .start(this.dailyStockDataStep())
                .validator(new DailyStockDataJobParametersValidator())
                .incrementer(new RunIdIncrementer())
                .build();
    }


    @Bean
    public Step dailyStockDataStep() {
        return stepBuilderFactory.get("dailyStockDataStep")
                .<DailyStock, DailyStock>chunk(100)
                .reader(this.dailyStockDataItemReader(null))
                .writer(this.dailyStockDataItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<DailyStock> dailyStockDataItemReader(@Value("#{jobParameters[dailyStockFile]}") Resource resource) {

        return new FlatFileItemReaderBuilder<DailyStock>()
                .name("dailyStockDataItemReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("marketDate", "openPrice", "highPrice", "lowPrice", "closePrice", "volume", "isOpen")
                .fieldSetMapper(new DailyStockDataFieldSetMapper(resource))
                .build();
    }

    @RequiredArgsConstructor
    public static class DailyStockDataFieldSetMapper implements FieldSetMapper<DailyStock> {
        private final Resource resource;

        @Override
        public DailyStock mapFieldSet(FieldSet fieldSet) throws BindException {
            LocalDate marketDate = fieldSet.readDate("marketDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            double openPrice = fieldSet.readDouble("openPrice");
            double highPrice = fieldSet.readDouble("highPrice");
            double closePrice = fieldSet.readDouble("closePrice");
            double lowPrice = fieldSet.readDouble("lowPrice");
            long volume = fieldSet.readLong("volume");

            String filename = resource.getFilename();
            String stockCode = filename.split("\\.")[0];

            return DailyStock.builder()
                    .marketDate(marketDate)
                    .openPrice(openPrice)
                    .highPrice(highPrice)
                    .closePrice(closePrice)
                    .lowPrice(lowPrice)
                    .stockCode(stockCode)
                    .volume(volume)
                    .build();
        }
    }

    @Bean
    public JpaItemWriter<DailyStock> dailyStockDataItemWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<DailyStock>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
