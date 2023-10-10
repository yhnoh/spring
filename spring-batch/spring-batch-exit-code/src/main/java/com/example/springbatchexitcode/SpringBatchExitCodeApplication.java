package com.example.springbatchexitcode;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
@RequiredArgsConstructor
public class SpringBatchExitCodeApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchExitCodeApplication.class, args)));
    }

    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return () -> 42;
    }

//    private final JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator;

//    @Bean
//    public ExitCodeGenerator exitCodeGenerator(){
//        return new JobExitCodeGenerator();
//    }
//
//    /**
//     * {@link ExitCodeGenerator} for {@link JobExecutionEvent}s.
//     * {@link org.springframework.boot.autoconfigure.batch.JobExecutionExitCodeGenerator}
//     * @author Dave Syer
//     * @since 1.0.0
//     */
//    class JobExitCodeGenerator implements ExitCodeGenerator, ApplicationListener<JobExecutionEvent> {
//        private JobExecution jobExecution;
//
//        @Override
//        public int getExitCode() {
//            return jobExecution.getStatus().ordinal();
//        }
//
//        @Override
//        public void onApplicationEvent(JobExecutionEvent jobExecutionEvent) {
//            this.jobExecution = jobExecutionEvent.getJobExecution();
//        }
//    }


}
