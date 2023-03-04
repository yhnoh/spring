package com.example.springbatchtest;

import com.example.springbatchtest.dto.DailyStockPriceDTO;
import com.example.springbatchtest.entity.DailyStockPrice;
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
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
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
                .<DailyStockPriceDTO, DailyStockPrice>chunk(10)
                .reader(this.dataItemReader())
                .processor(this.dataItemProcessor())
                .writer(this.dataItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<DailyStockPriceDTO> dataItemReader(){
        Resource resource = new ClassPathResource("./data/data.csv");

        return new FlatFileItemReaderBuilder<DailyStockPriceDTO>()
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
    public static class DailyStockPriceFieldSetMapper implements FieldSetMapper<DailyStockPriceDTO> {
        @Override
        public DailyStockPriceDTO mapFieldSet(FieldSet fieldSet) throws BindException {
            return DailyStockPriceDTO.builder()
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
    public ItemProcessor<DailyStockPriceDTO, DailyStockPrice> dataItemProcessor(){
        return item -> DailyStockPrice.builder()
                .marketDate(item.getMarketDate())
                .openPrice(item.getOpenPrice())
                .highPrice(item.getHighPrice())
                .closePrice(item.getClosePrice())
                .lowPrice(item.getLowPrice())
                .volume(item.getVolume())
                .build();
    }
    @Bean
    public JpaItemWriter<DailyStockPrice> dataItemWriter(EntityManagerFactory entityManagerFactory){
        return new JpaItemWriterBuilder<DailyStockPrice>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
