package com.example.springbatchitemreader.json_item_reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@Configuration
public class JsonItemReaderConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jsonItemReaderJob(){
        return jobBuilderFactory.get("jsonItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(jsonItemReaderStep())
                .build();
    }

    @Bean
    public Step jsonItemReaderStep(){
        return stepBuilderFactory.get("jsonItemReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(jsonItemReader(null))
                .writer(jsonItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Customer> jsonItemReader(@Value("#{jobParameters['customerFile']}") Resource resource) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

        JacksonJsonObjectReader<Customer> objectReader = new JacksonJsonObjectReader<>(Customer.class);
        objectReader.setMapper(objectMapper);

        return new JsonItemReaderBuilder<Customer>()
                .name("jsonItemReader")
                .jsonObjectReader(objectReader)
                .resource(resource)
                .build();

    }

    @Bean
    public ItemWriter<Customer> jsonItemWriter() {
        return items -> items.forEach(System.out::println);
    }

}
