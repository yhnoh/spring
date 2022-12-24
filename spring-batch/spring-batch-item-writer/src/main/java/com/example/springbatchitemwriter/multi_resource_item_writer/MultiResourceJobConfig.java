package com.example.springbatchitemwriter.multi_resource_item_writer;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
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
public class MultiResourceJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job multiResourceJob() {
        return jobBuilderFactory.get("multiResourceJob")
                .incrementer(new RunIdIncrementer())
                .start(this.multiResourceStep())
                .build();
    }

    @Bean
    public Step multiResourceStep() {
        return stepBuilderFactory.get("multiResourceStep")
                .<Customer, Customer>chunk(10)
                .reader(this.multiResourceItemReader(null))
                .writer(this.multiResourceItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> multiResourceItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("multiResourceItemReader")
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
    public StaxEventItemWriter<Customer> multiResourceStaxItemWriter() {
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);
        marshaller.afterPropertiesSet();

        return new StaxEventItemWriterBuilder<Customer>()
                .name("multiResourceStaxItemWriter")
                .marshaller(marshaller)
                .rootTagName("customers")
                .build();
    }

    /**
     * <pre>MultiResourceItemWriter</pre>
     * <p>처리한 레코드 수에 따라 출력 리소스를 동적으로 만든다.</p>
     * <p>예를 들어 100개의 아이템이 존재하고, itemCountLimitPerResource 메서드에 정의한 개수에 따라 파일을 분할하여 리소스를 만든다.</p>
     * <p>리소스 생성을 위한 임계값에 도달하면 현재 파일이 닫힌다.</p>
     *
     * <pre>MultiResourceItemWriter 구성 요소</pre>
     * <p>파일 생성 위치와 파일 이름</p>
     * <p>출력 파일에 실제로 쓰기 작업을 수행하는 delgateWirter</p>
     * <p>ItemWriter가 파일당 쓰기 작업을 수행할 아이템 수</p>
     * <p>새 파일을 생성할 때 생성할 파일의 확장자 명 (접미사)</p>
     */
    @Bean
    public MultiResourceItemWriter<Customer> multiResourceItemWriter() {
        return new MultiResourceItemWriterBuilder<Customer>()
                .name("multiResourceItemWriter")
                .delegate(this.multiResourceStaxItemWriter())
                .itemCountLimitPerResource(10)
                .resource(new FileSystemResource("./src/main/resources/output/multi_resource.xml"))
                .resourceSuffixCreator(index -> index + ".xml")
                .build();
    }
}
