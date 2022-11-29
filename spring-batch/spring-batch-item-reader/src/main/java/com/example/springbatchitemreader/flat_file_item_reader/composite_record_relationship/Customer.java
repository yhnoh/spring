package com.example.springbatchitemreader.flat_file_item_reader.composite_record_relationship;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
public class Customer {

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
}
