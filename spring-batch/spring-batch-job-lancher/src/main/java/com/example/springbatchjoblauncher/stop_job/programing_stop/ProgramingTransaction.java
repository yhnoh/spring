package com.example.springbatchjoblauncher.stop_job.programing_stop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ProgramingTransaction {

    private String accountNumber;
    private Date timestamp;
    private double amount;


}