package com.example.springbatchitemwriter.flat_file_item_writer.formatted;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Customer {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;

}
