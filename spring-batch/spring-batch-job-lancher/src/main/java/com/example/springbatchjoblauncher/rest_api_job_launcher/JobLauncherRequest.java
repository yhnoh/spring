package com.example.springbatchjoblauncher.rest_api_job_launcher;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

@Getter
@Setter
public class JobLauncherRequest {

    private String name;
    private Properties jobParameters;

    public void setJobParametersProperties(Properties properties){
        this.jobParameters = jobParameters;
    }

    public Properties getJobParametersProperties(){
        return this.jobParameters;
    }

    public JobParameters getJobParameters(){
        Properties properties = new Properties();
        properties.putAll(jobParameters);
        return new JobParametersBuilder(properties).toJobParameters();
    }

}
