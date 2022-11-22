package com.example.springbatchjoblauncher.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Transaction {

    private String accountNumber;
    private Date timestamp;
    private double amount;


}
