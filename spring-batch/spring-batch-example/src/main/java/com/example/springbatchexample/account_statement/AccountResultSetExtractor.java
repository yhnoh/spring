package com.example.springbatchexample.account_statement;

import com.example.springbatchexample.account_statement.dto.Account;
import com.example.springbatchexample.transaction.dto.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountResultSetExtractor implements ResultSetExtractor<List<Account>> {

    private List<Account> accounts = new ArrayList<>();
    private Account curAccount;

    @Nullable
    @Override
    public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {

        while (rs.next()) {
            if(curAccount == null) {
                curAccount = Account.builder()
                        .id(rs.getLong("account_id"))
                        .balance(rs.getBigDecimal("balance"))
                        .lastStatementDate(rs.getDate("last_statement_date"))
                        .build();
            }
            else if (rs.getLong("account_id") != curAccount.getId()) {
                accounts.add(curAccount);

                curAccount = Account.builder()
                        .id(rs.getLong("account_id"))
                        .balance(rs.getBigDecimal("balance"))
                        .lastStatementDate(rs.getDate("last_statement_date"))
                        .build();            }

            if(StringUtils.hasText(rs.getString("description"))) {

                Transaction transaction = new Transaction(rs.getLong("transaction_id"),
                        rs.getLong("account_id"),
                        rs.getString("description"),
                        rs.getBigDecimal("credit"),
                        rs.getBigDecimal("debit"),
                        new Date(rs.getTimestamp("timestamp").getTime()));
                curAccount.addTransaction(transaction);
            }
        }

        if(curAccount != null) {
            accounts.add(curAccount);
        }

        return accounts;
    }
}
