package com.example.springbatchjoblauncher.stop_job.account_step;

import com.example.springbatchjoblauncher.stop_job.AccountSummary;
import com.example.springbatchjoblauncher.stop_job.Transaction;
import com.example.springbatchjoblauncher.stop_job.repository.TransactionDao;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

public class TransactionApplierProcessor implements ItemProcessor<AccountSummary, AccountSummary> {
    private TransactionDao transactionDao;

    public TransactionApplierProcessor(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public AccountSummary process(AccountSummary accountSummary) throws Exception {
        List<Transaction> transactions = transactionDao.getTransactionByAccountNumber(accountSummary.getAccountNumber());

        for (Transaction transaction : transactions) {
            accountSummary.setCurrentBalance(accountSummary.getCurrentBalance()
                            + transaction.getAmount());
        }

        return accountSummary;
    }
}
