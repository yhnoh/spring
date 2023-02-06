package com.example.springbatchstock;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
@RequiredArgsConstructor
public class StockDataConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stockDataJob(){
        return jobBuilderFactory.get("stockDataJob")
                .start(this.stockDataStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }


    @Bean
    public Step stockDataStep(){
        return stepBuilderFactory.get("stockDataStep")
                .<Stock, Stock>chunk(100)
                .reader(this.stockDataItemReader())
                .writer(this.stockDataItemWriter(null))
                .build();
    }

    @Bean
    public FlatFileItemReader<Stock> stockDataItemReader(){
        ClassPathResource resource = new ClassPathResource("./data/stock/a.us.txt");
        return new FlatFileItemReaderBuilder<Stock>()
                .name("stockDataItemReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names(new String[]{"marketDate", "openPrice", "highPrice", "lowPrice", "closePrice", "volume", "isOpen"})
                .fieldSetMapper(new StockFieldSetMapper(resource))
                .build();
    }

    @RequiredArgsConstructor
    public static class StockFieldSetMapper implements FieldSetMapper<Stock> {
        private final Resource resource;

        @Override
        public Stock mapFieldSet(FieldSet fieldSet) throws BindException {
            LocalDate marketDate = fieldSet.readDate("marketDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            double openPrice = fieldSet.readDouble("openPrice");
            double highPrice = fieldSet.readDouble("highPrice");
            double closePrice = fieldSet.readDouble("closePrice");
            double lowPrice = fieldSet.readDouble("lowPrice");
            long volume = fieldSet.readLong("volume");

            String filename = resource.getFilename();
            String stockCode = filename.split("\\.")[0];

            return Stock.builder()
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
    public JpaItemWriter<Stock> stockDataItemWriter(EntityManagerFactory entityManagerFactory){
        return new JpaItemWriterBuilder<Stock>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
