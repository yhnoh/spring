package com.example.springbatchjob.step_flow.execution_decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Random;

public class RandomExecutionDecider implements JobExecutionDecider {

    private Random random = new Random();
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        return random.nextBoolean()
                ? new FlowExecutionStatus(FlowExecutionStatus.COMPLETED.getName())
                : new FlowExecutionStatus(FlowExecutionStatus.FAILED.getName());
    }
}
