package com.example.springbatchjoblauncher.stop_job.repository;

import com.example.springbatchjoblauncher.stop_job.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getTransactionByAccountNumber(String accountNumber);
}
