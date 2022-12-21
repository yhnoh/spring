package com.example.springbatchitemwriter.jms_item_writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.batch.item.jms.builder.JmsItemReaderBuilder;
import org.springframework.batch.item.jms.builder.JmsItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JmsJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JMS는 둘 이상의 엔드포인트 간에 통신하느 메시지지향적인 방식이다.
     * 발행 - 구독 모델을 사용해 다른 모든 기술과 통신할 수 있다.
     * --job.name=jmsJob inputFile=/input/customer.csv outputFile=./src/main/resources/output/jmsCustomer.xml
     * MessageConverter : 지점 간 전송을 위해 메시지를 JSON으로 변환한다.
     * JmsTemplate :
     */
    @Bean
    public Job jmsJob() {
        return jobBuilderFactory.get("jmsJob")
                .incrementer(new RunIdIncrementer())
                .start(this.jmsInputStep())
                .next(this.jmsOutputStep())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> jmsFileItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("delimiterFileItemReader")
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

    @Bean
    @StepScope
    public StaxEventItemWriter<Customer> jmsXmlItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile) {
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);
        marshaller.afterPropertiesSet();

        return new StaxEventItemWriterBuilder<Customer>()
                .name("jmsXmlItemWriter")
                .resource(outputFile)
                .rootTagName("customers")
                .marshaller(marshaller)
                .build();
    }

    @Bean
    public JmsItemReader<Customer> jmsItemReader(JmsTemplate jmsTemplate) {
        return new JmsItemReaderBuilder<Customer>()
                .jmsTemplate(jmsTemplate)
                .itemType(Customer.class)
                .build();
    }

    @Bean
    public JmsItemWriter<Customer> jmsItemWriter(JmsTemplate jmsTemplate) {
        return new JmsItemWriterBuilder<Customer>()
                .jmsTemplate(jmsTemplate)
                .build();
    }

    @Bean
    public Step jmsInputStep() {
        return stepBuilderFactory.get("jmsInputStep")
                .<Customer, Customer>chunk(10)
                .reader(this.jmsFileItemReader(null))
                .writer(this.jmsItemWriter(null))
                .build();
    }

    @Bean
    public Step jmsOutputStep() {
        return stepBuilderFactory.get("jmsOutputStep")
                .<Customer, Customer>chunk(10)
                .reader(this.jmsItemReader(null))
                .writer(this.jmsXmlItemWriter(null))
                .build();
    }
}