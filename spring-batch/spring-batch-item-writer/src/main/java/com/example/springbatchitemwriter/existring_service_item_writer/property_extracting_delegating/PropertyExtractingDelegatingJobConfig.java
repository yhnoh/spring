package com.example.springbatchitemwriter.existring_service_item_writer.property_extracting_delegating;

import com.example.springbatchitemwriter.existring_service_item_writer.Customer;
import com.example.springbatchitemwriter.existring_service_item_writer.ExistingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.PropertyExtractingDelegatingItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class PropertyExtractingDelegatingJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job propertyExtractingDelegatingJob() {
        return jobBuilderFactory.get("propertyExtractingDelegatingJob")
                .incrementer(new RunIdIncrementer())
                .start(this.propertyExtractingDelegatingStep())
                .build();
    }

    @Bean
    public Step propertyExtractingDelegatingStep() {
        return stepBuilderFactory.get("propertyExtractingDelegatingStep")
                .<Customer, Customer>chunk(10)
                .reader(this.propertyExtractingDelegatingItemReader(null))
                .writer(this.propertyExtractingDelegatingItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> propertyExtractingDelegatingItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("propertyExtractingDelegatingItemReader")
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .resource(inputFile)
                .build();
    }


    /**
     * <pre>PropertyExtractingDelegatingItemWriter<pre/>
     * 아이템에서 값을 추출한 후 이를 서비스에 파라미터로 전달하는 메커니즘을 제공한다.
     * 서비스 객체 및 호출할 메서드를 지정하고 아이템에서 추출할 프로퍼티 목록을 순서에 맞게 전달한다.
     */
    @Bean
    public PropertyExtractingDelegatingItemWriter<Customer> propertyExtractingDelegatingItemWriter(ExistingService existingService) {
        PropertyExtractingDelegatingItemWriter<Customer> itemWriter = new PropertyExtractingDelegatingItemWriter<>();
        itemWriter.setTargetObject(existingService);
        itemWriter.setTargetMethod("logCustomerAddress");
        itemWriter.setFieldsUsedAsTargetMethodArguments(new String[]{"address", "city", "state", "zip"});
        return itemWriter;
    }
}
