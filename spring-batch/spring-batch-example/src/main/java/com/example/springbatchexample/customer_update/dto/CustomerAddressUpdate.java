package com.example.springbatchexample.customer_update.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class CustomerAddressUpdate extends CustomerUpdate{

    private final String address1;
    private final String address2;
    private final String city;
    private final String state;
    private final String postalCode;

    public CustomerAddressUpdate(long customerId, String address1, String address2, String city, String state, String postalCode) {
        super(customerId);
        this.address1 = this.getNullWhenEmpty(address1);
        this.address2 = this.getNullWhenEmpty(address2);
        this.city = this.getNullWhenEmpty(city);
        this.state = this.getNullWhenEmpty(state);
        this.postalCode = this.getNullWhenEmpty(postalCode);
    }

    private String getNullWhenEmpty(String str){
        return StringUtils.hasText(str) ? str : null;
    }
}
