package com.example.springbatchitemreader.item_stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ItemStreamFlowJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public static final String JOB_NAME_PREFIX = "itemStreamFlow";

    @Bean(name = JOB_NAME_PREFIX + "Job")
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME_PREFIX + "Job").start(this.step()).build();
    }

    @Bean(name = JOB_NAME_PREFIX + "Step")
    public Step step() {
        return stepBuilderFactory.get(JOB_NAME_PREFIX + "Step")
                .<String, String>chunk(1)
                .reader(new ItemStreamFlowItemReader())
                .writer(items -> items.forEach(item -> log.info("write = {}", item)))
                .build();
    }

    @Slf4j
    public static class ItemStreamFlowItemReader implements ItemStreamReader<String> {

        private Iterator<String> iterator = List.of("1", "2", "3", "4", "5").iterator();
        String read = "";

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            read = iterator.hasNext() ? iterator.next() : null;
            log.info("read = {}", read);
            return read;
        }

        @Override
        public void open(ExecutionContext executionContext) throws ItemStreamException {
            log.info("open");
        }

        @Override
        public void update(ExecutionContext executionContext) throws ItemStreamException {
            log.info("update = {}", read);
        }

        @Override
        public void close() throws ItemStreamException {
            log.info("close");
        }
    }

}
