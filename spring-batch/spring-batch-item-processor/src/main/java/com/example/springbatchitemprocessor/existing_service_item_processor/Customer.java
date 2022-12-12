package com.example.springbatchitemprocessor.existing_service_item_processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

    private String zip;


    public static Customer newInstance(Customer customer) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(customer);
            return objectMapper.readValue(json, Customer.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
