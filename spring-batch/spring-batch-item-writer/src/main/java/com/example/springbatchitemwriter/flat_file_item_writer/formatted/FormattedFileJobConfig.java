package com.example.springbatchitemwriter.flat_file_item_writer.formatted;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class FormattedFileJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job formattedFileJob(){
        return jobBuilderFactory.get("formattedFileJob")
                .incrementer(new RunIdIncrementer())
                .start(formattedFileStep())
                .build();
    }

    @Bean
    public Step formattedFileStep(){
        return stepBuilderFactory.get("formattedFileStep")
                .<Customer, Customer>chunk(10)
                .reader(this.formattedFileItemReader(null))
                .writer(this.formattedFileItemWriter(null))
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> formattedFileItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile){

        return new FlatFileItemReaderBuilder<Customer>()
                .name("formattedFileItemReader")
                .delimited()
                .names(new String[] {"firstName",
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
     *
     * <pre>형식화된 문자열 파일로 출력</pre>
     * <p>ProgramArgument : --job.name=formattedFileJob inputFile=/input/customer.csv outputFile=./src/main/resources/output/formattedCustomer.txt</p>
     * <p>Resource 인터페이스 사용 시, ClassPathResource 사용 -> 파일이 없을 경우 에러 발생 및 있을 경우 build 폴더에 생성</p>
     * <p>FileSystemResource 사용시 내가 원하는 폴더에 파일 생성, 하지만 전체 경로를 사용해야함</p>
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> formattedFileItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile){
        		return new FlatFileItemWriterBuilder<Customer>()
				.name("formattedFileItemWriter")
				.resource(outputFile)
				.formatted()
				.format("%s %s lives at %s %s in %s, %s.")
				.names(new String[] {"firstName", "lastName", "address", "city", "state", "zip"})
				.build();
    }
}
