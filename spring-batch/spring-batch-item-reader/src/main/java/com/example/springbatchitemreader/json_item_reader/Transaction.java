package com.example.springbatchitemreader.json_item_reader;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Transaction {

    private String accountNumber;
    private Date transactionDate;
    private Double amount;


}
