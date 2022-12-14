package com.example.springbatchitemprocessor.custom_item_processor;

import org.springframework.batch.item.ItemProcessor;

public class EvenFilteringItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {
        return Integer.parseInt(item.getZip()) % 2 == 0 ? null :item;
    }
}
