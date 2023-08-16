package com.example.springvalidation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 공백이 있으면 안됩니다.
 * 패스워드는 숫자 영어로 이뤄줘야 합니다.
 * 길이는 2 ~ 16글자
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern ALPHABET_PATTERN = Pattern.compile("[a-zA-Z]");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            return false;
        }

        if (StringUtils.containsWhitespace(value)) {
            return false;
        }

        boolean isLength = value.length() >= 2 && value.length() <= 16;
        boolean isNumber = NUMBER_PATTERN.matcher(value).find();
        boolean isAlphabet = ALPHABET_PATTERN.matcher(value).find();
        return isLength && isNumber && isAlphabet;
    }

    @Override
    public void initialize(Password constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
