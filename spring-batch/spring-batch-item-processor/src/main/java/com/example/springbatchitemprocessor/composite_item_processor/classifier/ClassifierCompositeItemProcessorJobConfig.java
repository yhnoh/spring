package com.example.springbatchitemprocessor.composite_item_processor.classifier;

import com.example.springbatchitemprocessor.composite_item_processor.Customer;
import com.example.springbatchitemprocessor.composite_item_processor.UniqueLastNameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ScriptItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class ClassifierCompositeItemProcessorJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job classifierCompositeItemProcessorJob(){
        return jobBuilderFactory.get("classifierCompositeItemProcessorJob")
                .start(this.classifierCompositeItemProcessorStep())
                .build();
    }

    @Bean
    public Step classifierCompositeItemProcessorStep(){
        return stepBuilderFactory.get("classifierCompositeItemProcessorStep")
                .<Customer,Customer>chunk(5)
                .reader(this.classifierCompositeItemReader(null))
                .processor(this.classifierCompositeItemProcessor())
                .writer(this.classifierCompositeItemWriter())
                .stream(this.classifierUniqueLastNameValidator())
                .build();

    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> classifierCompositeItemReader(@Value("#{jobParameters['customerFile']}") Resource customerFile){

        return new FlatFileItemReaderBuilder<Customer>()
                .name("classifierCompositeItemReader")
                .delimited()
                .names(new String[] {"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .resource(customerFile)
                .build();
    }
    @Bean
    public ItemWriter<Customer> classifierCompositeItemWriter(){
        return items -> items.forEach(System.out::println);
    }

    @Bean
    @StepScope
    public ScriptItemProcessor<Customer, Customer> classifierUpperCaseItemProcessor(@Value("#{jobParameters['script']}") Resource script){
        ScriptItemProcessor<Customer, Customer> itemProcessor = new ScriptItemProcessor<>();
        itemProcessor.setScript(script);
        return itemProcessor;
    }

    @Bean
    public ValidatingItemProcessor<Customer> classifierUniqueLastNameValidatingItemProcessor(){
        ValidatingItemProcessor<Customer> itemProcessor = new ValidatingItemProcessor<>(this.classifierUniqueLastNameValidator());
        //유효성 검증을 통과하지 못한 아이템을 걸러낸다.
        itemProcessor.setFilter(true);
        return itemProcessor;
    }

    @Bean
    public UniqueLastNameValidator classifierUniqueLastNameValidator(){
        UniqueLastNameValidator uniqueLastNameValidator = new UniqueLastNameValidator();
        uniqueLastNameValidator.setName("classifierUniqueLastNameValidator");
        return uniqueLastNameValidator;
    }

    @Bean
    public Classifier classifier(){
        return new ZipCodeClassifier(classifierUpperCaseItemProcessor(null),
                classifierUniqueLastNameValidatingItemProcessor());
    }

    /**
     * 모든 상황에 ItemProcessor를 적용하는 것이 아닌, 각 상황에 맞게끔 ItemProcessor를 적용한다.
     */
    @Bean
    public ClassifierCompositeItemProcessor<Customer, Customer> classifierCompositeItemProcessor(){
        ClassifierCompositeItemProcessor<Customer, Customer> itemProcessor = new ClassifierCompositeItemProcessor<>();
        itemProcessor.setClassifier(classifier());
        return itemProcessor;
    }

}
