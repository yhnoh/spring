package com.example.springbatchjoblauncher.stop_job.step_execution_stop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class StepExecutionTransaction {

    private String accountNumber;
    private Date timestamp;
    private double amount;


}