package com.example.springbatchitemwriter.stax_event_item_writer;

import com.example.springbatchitemwriter.stax_event_item_writer.header_footer.XmlFileHeaderCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class XmlFileJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final XmlFileHeaderCallback xmlFileHeaderCallback;

    @Bean
    public Job xmlFileJob() {
        return jobBuilderFactory.get("xmlFileJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlFileStep())
                .build();
    }

    @Bean
    public Step xmlFileStep() {
        return stepBuilderFactory.get("xmlFileStep")
                .<Customer, Customer>chunk(10)
                .reader(this.xmlFileItemReader(null))
                .writer(this.xmlFileItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> xmlFileItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("xmlFileItemReader")
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
     * <pre>xml 파일로 출력</pre>
     * <p>ProgramArgument : --job.name=xmlFileJob inputFile=/input/customer.csv outputFile=./src/main/resources/output/formattedCustomer.xml</p>
     * <p>출력 데이터를 기록할 리소스, 루트 엘리멘트 이름, Marshaller로 구성</p>
     * <p>Marshaller를 이용해 아이템을 xml형식으로 구성해준다.</p>
     *
     * <pre>필요 의존성</pre>
     * implementation 'org.springframework:spring-oxm' <br/></>
     * implementation 'com.thoughtworks.xstream:xstream:1.4.19'
     */
    @Bean
    @StepScope
    public StaxEventItemWriter<Customer> xmlFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile) {
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);

        marshaller.afterPropertiesSet();

        return new StaxEventItemWriterBuilder<Customer>()
                .name("xmlFileItemWriter")
                .resource(outputFile)
                .rootTagName("customers")
                .headerCallback(xmlFileHeaderCallback)
                .marshaller(marshaller)
                .build();
    }
}
