package com.example.springbatchitemreader.existing_service_item_reader;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExistingServiceItemReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerService customerService;

    @Bean
    public Job existingServiceItemReaderJob() {
        return jobBuilderFactory.get("existingServiceItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(existingServiceItemReaderStep())
                .build();
    }

    @Bean
    public Step existingServiceItemReaderStep() {
        return stepBuilderFactory.get("existingServiceItemReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(existingServiceItemReaderAdapter())
                .writer(existingServiceItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<Customer> existingServiceItemWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public ItemReaderAdapter<Customer> existingServiceItemReaderAdapter() {
        ItemReaderAdapter<Customer> adapter = new ItemReaderAdapter<>();

        adapter.setTargetObject(customerService);
        adapter.setTargetMethod("getCustomer");
        return adapter;
    }
}
