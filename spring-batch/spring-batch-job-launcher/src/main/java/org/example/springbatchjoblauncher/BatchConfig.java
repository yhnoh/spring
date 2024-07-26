package org.example.springbatchjoblauncher;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class BatchConfig {

    @Bean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
            JobRepository jobRepository, BatchProperties properties) {
        JobLauncherApplicationRunner runner =
                new DefaultJobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
        String jobName = properties.getJob().getName();
        if(StringUtils.hasText(jobName)) {
            runner.setJobName(jobName);
        }
        return runner;
    }
}
