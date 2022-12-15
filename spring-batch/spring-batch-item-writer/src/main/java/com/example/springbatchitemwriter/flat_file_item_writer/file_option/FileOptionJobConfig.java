package com.example.springbatchitemwriter.flat_file_item_writer.file_option;

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
public class FileOptionJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job FileOptionJob(){
        return jobBuilderFactory.get("FileOptionJob")
                .incrementer(new RunIdIncrementer())
                .start(FileOptionStep())
                .build();
    }

    @Bean
    public Step FileOptionStep(){
        return stepBuilderFactory.get("FileOptionStep")
                .<Customer, Customer>chunk(10)
                .reader(this.FileOptionItemReader(null))
                .writer(this.FileOptionItemWriter(null))
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> FileOptionItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile){

        return new FlatFileItemReaderBuilder<Customer>()
                .name("FileOptionItemReader")
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
     * <pre>파일 관리 옵션</pre>
     * <pre>shouldDeleteIfEmpty default=false</pre>
     * shouldDeleteIfEmpty가 true로 설정되어 있을 경우 아무 아이템이 쓰여지지 않느다면 출력할 파일을 삭제한다. <br/>
     *
     * <pre>shouldDeleteIfExists default=true</pre>
     * 출력할 파일과 같은 이름이 존재한다면 해당 파일을 삭제한다. <br/>
     * 만약 false로 설정되어 있고 파일 생성을 시돤다면 ItemStreamException이 발생한다.<br/>
     * 이전 출력 결과 파일을 실수로 덮어 쓰는 것을 방지할 수 있다.
     *
     * <pre>append default=false</pre>
     * true로 설정할 경우 파일이 존재하지 않으면 파일을 새로 생성하여 쓰기 작업을 진행하고<br/>
     * 만약 파일이 존재할 경우 추가적으로 데이터를 삽입한다.<br/>
     * true로 설정할 경우 shouldDeleteIfExists 플래그를 false로 자동 설정한다.<br/>
     * 하나의 출력 파일에 쓰기 작업을 할 경우 유용하다.
     */
    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> FileOptionItemWriter(@Value("#{jobParameters['outputFile']}") FileSystemResource outputFile){
        return new FlatFileItemWriterBuilder<Customer>()
                .name("FileOptionItemWriter")
                .resource(outputFile)
                .delimited()
                .delimiter(";")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
//                .shouldDeleteIfEmpty(false)
//                .shouldDeleteIfExists(true)
//                .append(false)
                .build();
    }
}
