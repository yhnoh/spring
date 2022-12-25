package com.example.springbatchitemwriter.classifier_composite_item_writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;


public class ItemWriterClassifier implements Classifier<Customer, ItemWriter<? super Customer>> {

    private ItemWriter<Customer> fileItemWriter;
    private ItemWriter<Customer> jdbcItemWriter;

    public ItemWriterClassifier(ItemWriter<Customer> fileItemWriter, ItemWriter<Customer> jdbcItemWriter) {
        this.fileItemWriter = fileItemWriter;
        this.jdbcItemWriter = jdbcItemWriter;
    }

    @Override
    public ItemWriter<Customer> classify(Customer customer) {
        if (customer.getState().matches("^[A-M].*")) {
            return fileItemWriter;
        } else {
            return jdbcItemWriter;
        }
    }
}
