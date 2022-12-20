package com.example.springbatchitemwriter.database_item_writer.jdbc;

import com.example.springbatchitemwriter.database_item_writer.Customer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcItemPreparedStatementSetter implements ItemPreparedStatementSetter<Customer> {

    @Override
    public void setValues(Customer customer, PreparedStatement ps) throws SQLException {
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getMiddleInitial());
        ps.setString(3, customer.getLastName());
        ps.setString(4, customer.getAddress());
        ps.setString(5, customer.getCity());
        ps.setString(6, customer.getState());
        ps.setString(7, customer.getZip());
    }
}
