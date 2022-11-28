package com.example.springbatchitemreader.flat_file_item_reader.fixed_length;


import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String firstName;
    private String middleInitial;
    private String lastName;
    private String addressNumber;
	private String street;
    private String address;
    private String city;
    private String state;
    private String zipCode;


}
