package com.example.springbatchexample.customer_update.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdate {
    protected final long customerId;

    public CustomerUpdate(long customerId) {
        this.customerId = customerId;
    }
}
