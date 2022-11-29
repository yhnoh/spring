package com.example.springbatchitemreader.flat_file_item_reader.composite_record_relationship;

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
