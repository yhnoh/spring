package com.example.springbatchjob.Intercepting_job_execution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class InterceptingJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("before job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("after job status : COMPLETED");
        }else if(jobExecution.getStatus() == BatchStatus.FAILED){
            log.info("after job status : FAILED");
        }
        log.info("after job");
    }
}
