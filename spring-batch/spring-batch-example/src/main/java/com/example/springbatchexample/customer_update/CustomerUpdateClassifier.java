package com.example.springbatchexample.customer_update;

import com.example.springbatchexample.customer_update.dto.CustomerAddressUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerContactUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerNameUpdate;
import com.example.springbatchexample.customer_update.dto.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.classify.Classifier;

@RequiredArgsConstructor
public class CustomerUpdateClassifier implements Classifier<CustomerUpdate, ItemWriter<? super CustomerUpdate>> {

    private final JdbcBatchItemWriter<CustomerUpdate> customerNameUpdateItemWriter;
    private final JdbcBatchItemWriter<CustomerUpdate> customerAddressUpdateItemWriter;
    private final JdbcBatchItemWriter<CustomerUpdate> customerContactUpdateItemWriter;

    @Override
    public ItemWriter<? super CustomerUpdate> classify(CustomerUpdate customer) {
        if(customer instanceof CustomerNameUpdate){
            return customerNameUpdateItemWriter;
        }else if (customer instanceof CustomerAddressUpdate){
            return customerAddressUpdateItemWriter;
        }else if (customer instanceof CustomerContactUpdate){
            return customerContactUpdateItemWriter;
        }else{
            throw new IllegalArgumentException("Invalid type : " + customer.getClass().getCanonicalName());
        }
    }
}
