package com.example.springbatchitemreader.flat_file_item_reader.delimited_fieldset_mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class DelimitedQuotationFieldSetMapper implements FieldSetMapper<Customer> {

    /**
     * # 인용구 다시 FieldSetMapper로 커스텀 매핑 작업 진행
     */
    @Override
    public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
        Customer customer = new Customer();
        String[] streets = fieldSet.readString("street").split(",");
        customer.setAddress(streets[0] + " " + streets[1]);
        customer.setCity(fieldSet.readString("city"));
        customer.setFirstName(fieldSet.readString("firstName"));
        customer.setLastName(fieldSet.readString("lastName"));
        customer.setMiddleInitial(fieldSet.readString("middleInitial"));
        customer.setState(fieldSet.readString("state"));
        customer.setZipCode(fieldSet.readString("zipCode"));

        return customer;
    }
}
