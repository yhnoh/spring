package com.example.springbatchitemwriter.classifier_composite_item_writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class ClassifierCompositeJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job classifierCompositeJob() {
        return jobBuilderFactory.get("classifierCompositeJob")
                .incrementer(new RunIdIncrementer())
                .start(this.classifierCompositeStep())
                .build();
    }

    @Bean
    public Step classifierCompositeStep() {
        return stepBuilderFactory.get("classifierCompositeStep")
                .<Customer, Customer>chunk(10)
                .reader(this.classifierCompositeItemReader(null))
                .writer(this.classifierCompositeItemWriter())
                .stream(this.classifierCompositeFileItemWriter(null))
                .build();
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Customer> classifierCompositeItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("classifierCompositeItemReader")
                .resource(inputFile)
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .build();
    }

    /**
     * <pre>ClassifierCompositeItemWriter</pre>
     * <p>
     * ClassifierCompositeItemWriter를 사용해 미리 정한 기준에 따라 ItemWriter를 선택적으로 사용할 수 있다. <br/>
     * ClassifierCompositeItemWriter는 ItemStream 인터페이스를 구현하지 않기 때문에 입출력에 파일을 사용한다면
     * 해당 Step에 ItemStream을 넣어주어야한다.
     */
    @Bean
    public ClassifierCompositeItemWriter<Customer> classifierCompositeItemWriter() {
        ItemWriterClassifier classifier = new ItemWriterClassifier(this.classifierCompositeFileItemWriter(null),
                this.classifierCompositeJdbcItemWriter());

        return new ClassifierCompositeItemWriterBuilder<Customer>()
                .classifier(classifier)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> classifierCompositeJdbcItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .dataSource(dataSource)
                .sql("INSERT INTO CUSTOMER (first_name, " +
                        "middle_initial, " +
                        "last_name, " +
                        "address, " +
                        "city, " +
                        "state, " +
                        "zip) VALUES (:firstName, :middleInitial, :lastName, :address, :city, :state, :zip)")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> classifierCompositeFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile) {
        return new FlatFileItemWriterBuilder<Customer>()
                .name("classifierCompositeFileItemWriter")
                .resource(outputFile)
                .formatted()
                .format("%s %s lives at %s %s in %s, %s.")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
                .build();
    }
}
