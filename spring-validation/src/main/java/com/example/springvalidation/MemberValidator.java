package com.example.springvalidation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MemberValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        Class<Member> memberClass = Member.class;
        return memberClass.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "field.required", "빈 문자열입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
    }
}
