package com.example.springbatchitemreader.flat_file_item_reader.multi_resource;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class MultiResourceConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job multiResourceJob(){
        return jobBuilderFactory.get("multiResourceJob")
                .incrementer(new RunIdIncrementer())
                .start(multiResourceStep())
                .build();
    }

    @Bean
    public Step multiResourceStep(){
        return stepBuilderFactory.get("multiResourceStep")
                .<Customer, Customer>chunk(10)
                .reader(multiResourceItemReader(null))
                .writer(multiResourceItemWriter())
                .build();
    }

    @Bean
    public ItemWriter multiResourceItemWriter() {
        return items -> {items.forEach(System.out::println);};
    }
    
    @Bean
    @StepScope
    public MultiResourceItemReader multiResourceItemReader(@Value("#{jobParameters['customerFile']}") Resource[] inputFiles){
        return new MultiResourceItemReaderBuilder<>()
                .name("multiResourceItemReader")
                .resources(inputFiles)
                .delegate(resourceCompositeRecordItemReader())
                .build();
    }
    
    @Bean
    public CompositeRecordItemReader resourceCompositeRecordItemReader(){
        return new CompositeRecordItemReader(resourceItemReader());
    }
    
    @Bean
    @StepScope
    public FlatFileItemReader resourceItemReader(){
        return new FlatFileItemReaderBuilder<Customer>()
                .name("resourceItemReader")
                .lineMapper(resourceLineMapper())
                .build();
    }
    
    @Bean
    public PatternMatchingCompositeLineMapper resourceLineMapper() {
        //파일에서 읽어 들어온 내용을 tokenizer함
        Map<String, LineTokenizer> tokenizers = new HashMap<>();

        tokenizers.put("CUST*", resourceCustomerLineTokenizer());
        tokenizers.put("TRANS*", resourceTransactionLineTokenizer());

        Map<String, FieldSetMapper> fieldSetMappers = new HashMap<>();

        //tokenizer를 통해서 매핑된 내용들을 각 클래스에 매핑
        BeanWrapperFieldSetMapper<Customer> customerFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        customerFieldSetMapper.setTargetType(Customer.class);

        fieldSetMappers.put("CUST*", customerFieldSetMapper);
        fieldSetMappers.put("TRANS*", new TransactionFieldSetMapper());

        PatternMatchingCompositeLineMapper lineMapper = new PatternMatchingCompositeLineMapper();

        lineMapper.setTokenizers(tokenizers);
        lineMapper.setFieldSetMappers(fieldSetMappers);

        return lineMapper;
    }

    @Bean
    public LineTokenizer resourceTransactionLineTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //접두어 필드 셋팅
        tokenizer.setNames("prefix", "accountNumber", "transactionDate", "amount");
        return tokenizer;
    }

    @Bean
    public LineTokenizer resourceCustomerLineTokenizer() {

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setNames("firstName", "middleInitial", "lastName", "address", "city", "state", "zipCode");
        //CUST 값 제외하고 셋팅, 접두어 필드 값 제외
        tokenizer.setIncludedFields(1,2,3,4,5,6,7);
        return tokenizer;
    }
}
