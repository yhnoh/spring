package com.example.springbatchitemreader.flat_file_item_reader.composite_record_relationship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
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
@Slf4j
public class CompositeLineMapperRelationshipJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job compositeLineMapperRelationshipJob(){
        return jobBuilderFactory.get("compositeLineMapperRelationshipJob")
                .incrementer(new RunIdIncrementer())
                .start(compositeLineMapperRelationshipStep())
                .build();
    }

    /**
     * 단일 파일이지만 고객과 거래 내역이 관계를 가지는 레코드일 때
     * ItemReader를 커스터마이징하여 사용할 수 있다.
     *
     * 레코드를 읽을 때는 고객 레코드와 이를 뒤따르는 거래 레코드를 단일 아이템을로 취급하다.
     * ExecutionContext에서 고객 레코드 수만큼만 읽었다는 처리를 한다.
     *
     */
    @Bean
    public CompositeRecordItemReader compositeRecordItemReader(){
        return new CompositeRecordItemReader(compositeLineMapperRelationshipItemReader(null));
    }
    @Bean
    public Step compositeLineMapperRelationshipStep(){
        return stepBuilderFactory.get("compositeLineMapperRelationshipStep")
                .<Customer, Customer>chunk(10)
                .reader(compositeRecordItemReader())
                .writer(compositeLineMapperRelationshipItemWriter())
                .build();
    }

    @Bean
    public ItemWriter compositeLineMapperRelationshipItemWriter() {
        return items -> {items.forEach(System.out::println);};
    }

    /**
     * 단일 파일에서 여로 레코들을 읽어서 처리하는 기능 제작
     * 개별 레코드 사이에서 관계가 없다고 가정하여 제작함
     */
    @Bean
    @StepScope
    public FlatFileItemReader compositeLineMapperRelationshipItemReader(@Value("#{jobParameters['customerFile']}") Resource resource){
        return new FlatFileItemReaderBuilder<Customer>()
                .name("compositeLineMapperRelationshipItemReader")
                .lineMapper(compositeLineMapperRelationship())
                .resource(resource)
                .build();
    }

    @Bean
    public PatternMatchingCompositeLineMapper compositeLineMapperRelationship() {
        //파일에서 읽어 들어온 내용을 tokenizer함
        Map<String, LineTokenizer> tokenizers = new HashMap<>();

        tokenizers.put("CUST*", customerLineTokenizerRelationship());
        tokenizers.put("TRANS*", transactionLineTokenizerRelationship());

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
    public LineTokenizer transactionLineTokenizerRelationship() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //접두어 필드 셋팅
        tokenizer.setNames("prefix", "accountNumber", "transactionDate", "amount");
        return tokenizer;
    }

    @Bean
    public LineTokenizer customerLineTokenizerRelationship() {

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setNames("firstName", "middleInitial", "lastName", "address", "city", "state", "zipCode");
        //CUST 값 제외하고 셋팅, 접두어 필드 값 제외
        tokenizer.setIncludedFields(1,2,3,4,5,6,7);
        return tokenizer;
    }
}
