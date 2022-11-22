package com.example.springbatchjoblauncher.transaction.repository;

import com.example.springbatchjoblauncher.transaction.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getTransactionByAccountNumber(String accountNumber);
}
