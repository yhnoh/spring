package com.example.springbatchexample.account_statement.dto;

import com.example.springbatchexample.transaction.dto.Transaction;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class Account {
    private long id;
    private BigDecimal balance;
    private Date lastStatementDate;
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();


    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
