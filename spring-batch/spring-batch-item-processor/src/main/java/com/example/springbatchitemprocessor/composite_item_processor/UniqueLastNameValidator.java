package com.example.springbatchitemprocessor.composite_item_processor;

import com.example.springbatchitemprocessor.composite_item_processor.Customer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UniqueLastNameValidator extends ItemStreamSupport implements Validator<Customer> {

    private Set<String> lastNames = new HashSet<>();

    @Override
    public void validate(Customer value) throws ValidationException {
        if(lastNames.contains(value.getLastName())){
            throw new ValidationException("Duplicate last name was found : " + value.getLastName());
        }
        this.lastNames.add(value.getLastName());
    }

    @Override
    public void open(ExecutionContext executionContext) {
        String lastNames = getExecutionContextKey("lastNames");
        if(executionContext.containsKey(lastNames)){
            this.lastNames = ObjectUtils.isEmpty(executionContext.get("lastNames")) ? new HashSet<>() : (Set<String>) executionContext.get("lastNames");
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
        Iterator<String> iterator = lastNames.iterator();
        Set<String> copedLastNames = new HashSet<>();
        while (iterator.hasNext()){
            copedLastNames.add(iterator.next());
        }

        executionContext.put(getExecutionContextKey("lastNames"), copedLastNames);
    }
}
