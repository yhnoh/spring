package com.example.springbatchitemreader.flat_file_item_reader.fixed_length;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class FixedLengthReaderJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job fixedLengthReaderJob(){
        return this.jobBuilderFactory.get("fixedLengthReaderJob")
                .start(fixedLengthReaderStep())
                .build();
    }

    @Bean
    public Step fixedLengthReaderStep(){
        return this.stepBuilderFactory.get("fixedLengthReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(this.fixedLengthItemReader(null))
                .writer(this.fixedLengthItemWriter())
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> fixedLengthItemReader(@Value("#{jobParameters['customerFile']}")Resource inputFile){
        return new FlatFileItemReaderBuilder<Customer>()
                .name("fixedLengthItemReader")
                .resource(inputFile)
                .fixedLength()
                .columns(new Range[]{new Range(1,11), new Range(12, 12), new Range(13, 22),
						new Range(23, 26), new Range(27,46), new Range(47,62), new Range(63,64),
						new Range(65,69)})
				.names(new String[] {"firstName", "middleInitial", "lastName",
						"addressNumber", "street", "city", "state","zipCode"})
                .targetType(Customer.class)
                .build();
    }

    @Bean
    public ItemWriter<Customer> fixedLengthItemWriter(){
        return items -> items.forEach(System.out::println);
    }
}
