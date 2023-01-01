package com.example.springbatchexample.customer_update;

import com.example.springbatchexample.customer_update.dto.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomerUpdateValidator implements Validator<CustomerUpdate> {

    static final String FIND_CUSTOMER = "SELECT COUNT(*) FROM CUSTOMER WHERE customer_id = :id";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void validate(CustomerUpdate customer) throws ValidationException {
        Map<String, Long> parameterMap = Collections.singletonMap("id", customer.getCustomerId());

        Long count = jdbcTemplate.queryForObject(FIND_CUSTOMER, parameterMap, Long.class);

        if(count == 0){
            throw new ValidationException(String.format("Customer id %s was not able to be found", customer.getCustomerId()));
        }
    }
}
