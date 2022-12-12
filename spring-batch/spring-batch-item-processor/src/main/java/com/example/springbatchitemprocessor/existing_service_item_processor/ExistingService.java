package com.example.springbatchitemprocessor.existing_service_item_processor;

import org.springframework.stereotype.Service;

@Service
public class ExistingService {
    public Customer upperCase(Customer customer){
        Customer newCustomer = Customer.newInstance(customer);

        newCustomer.setFirstName(newCustomer.getFirstName().toUpperCase());
        newCustomer.setMiddleInitial(newCustomer.getMiddleInitial().toUpperCase());
        newCustomer.setLastName(newCustomer.getLastName().toUpperCase());

        return newCustomer;
    }
}
