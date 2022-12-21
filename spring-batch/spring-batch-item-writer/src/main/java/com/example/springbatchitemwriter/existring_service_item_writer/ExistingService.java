package com.example.springbatchitemwriter.existring_service_item_writer;


import org.springframework.stereotype.Service;

@Service
public class ExistingService {

    public void logCustomer(Customer customer) {
        System.out.println("i just saved " + customer);
    }

    public void logCustomerAddress(String address, String city, String state, String zip) {
        String str = String.format("I just saved the address: %s, %s, %s, %s", address, city, state, zip);
        System.out.println(str);
    }
}
