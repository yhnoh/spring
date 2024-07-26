package org.example.springbatchjoblauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.core.log.LogMessage;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;

public class DefaultJobLauncherApplicationRunner extends JobLauncherApplicationRunner {

    private static final Log logger = LogFactory.getLog(DefaultJobLauncherApplicationRunner.class);
    private JobParametersConverter converter = new DefaultJobParametersConverter();
    private Collection<Job> jobs;
    private List<String> jobNames = new ArrayList<>();


    public DefaultJobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
            JobRepository jobRepository) {
        super(jobLauncher, jobExplorer, jobRepository);
    }


    @Override
    public void afterPropertiesSet() {

        Assert.isTrue(jobs.size() != 0, "jobs empty");
        Assert.isTrue(jobNames.size() != 0, "jabNames empty");
        Assert.isTrue(!this.isLocalJob(), "No job found with name '" + jobNames.toString() + "'");
    }


    @Autowired(required = false)
    public void setJobParametersConverter(JobParametersConverter converter) {
        super.setJobParametersConverter(converter);
        this.converter = converter;
    }


    @Autowired(required = false)
    public void setJobs(Collection<Job> jobs) {
        super.setJobs(jobs);
        this.jobs = jobs;
    }


    @Override
    public void setJobName(String jobName) {
        super.setJobName(jobName);
        this.jobNames = Arrays.stream(jobName.split(",")).toList();
    }


    protected void launchJobFromProperties(Properties properties) throws JobExecutionException {
        JobParameters jobParameters = this.converter.getJobParameters(properties);

        //jobNames 순서대로 잡을 실행
        for(String jobName : jobNames) {
            Optional<Job> findJob =
                    jobs.stream().filter(job -> PatternMatchUtils.simpleMatch(jobName, job.getName())).findAny();
            if(findJob.isEmpty()) {
                logger.debug(LogMessage.format("Skipped job: %s", jobName));
                continue;
            }

            super.execute(findJob.get(), jobParameters);
        }
    }


    private boolean isLocalJob() {
        return jobNames.stream().allMatch(jobName -> jobs.contains(jobName));
    }

}
