package com.example.springbatchitemwriter.jms_item_writer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;

}
