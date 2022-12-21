package com.example.springbatchitemwriter.existring_service_item_writer.adpater;

import com.example.springbatchitemwriter.existring_service_item_writer.Customer;
import com.example.springbatchitemwriter.existring_service_item_writer.ExistingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@RequiredArgsConstructor
@Configuration
public class AdapterJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job adapterJob() {
        return jobBuilderFactory.get("adapterJob")
                .incrementer(new RunIdIncrementer())
                .start(this.adapterStep())
                .build();
    }

    @Bean
    public Step adapterStep() {
        return stepBuilderFactory.get("adapterStep")
                .<Customer, Customer>chunk(10)
                .reader(this.adapterItemReader(null))
                .writer(this.adapterItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> adapterItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("adapterItemWriter")
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
     * <pre>ItemWriterAdpater<pre/>
     * ItemWriterAdpater는 기존 서비스를 살짝 감싼 래퍼일 뿐이다.
     * 기존 스프링 서비스를 ItmerWriter에 사용하기 위해서 ItemWriterAdpater를 활용
     */
    @Bean
    public ItemWriterAdapter<Customer> adapterItemWriter(ExistingService existingService) {
        ItemWriterAdapter<Customer> customerItemWriterAdapter = new ItemWriterAdapter<>();

        customerItemWriterAdapter.setTargetObject(existingService);
        customerItemWriterAdapter.setTargetMethod("logCustomer");

        return customerItemWriterAdapter;
    }
}
