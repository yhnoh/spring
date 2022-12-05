package com.example.springbatchitemreader.xml_item_reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@RequiredArgsConstructor
public class XmlItemReaderConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job xmlItemReaderJob(){
        return jobBuilderFactory.get("xmlItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlItemReaderStep())
                .build();
    }

    @Bean
    public Step xmlItemReaderStep(){
        return stepBuilderFactory.get("xmlItemReaderStep")
                .<Customer, Customer>chunk(10)
                .reader(xmlItemReader(null))
                .writer(xmlItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<Customer> xmlItemReader(@Value("#{jobParameters['customerFile']}") Resource resource){
        return new StaxEventItemReaderBuilder<Customer>()
                .name("xmlItemReader")
                .resource(resource)
                .addFragmentRootElements("customer")
                .unmarshaller(xmlUnmarshaller())
                .build();
    }

    @Bean
    public Unmarshaller xmlUnmarshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(Customer.class, Transaction.class);
        return jaxb2Marshaller;
    }



    @Bean
    public ItemWriter<Customer> xmlItemWriter(){
        return items -> items.forEach(System.out::println);
    }
}
