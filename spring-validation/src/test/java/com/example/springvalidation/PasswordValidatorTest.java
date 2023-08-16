package com.example.springvalidation;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PasswordValidatorTest {


    @Test
    void isValidTest() {
        //given
        PasswordValidator passwordValidator = new PasswordValidator();

        //when
        assertThat(passwordValidator.isValid(null, null)).isFalse();
        assertThat(passwordValidator.isValid("", null)).isFalse();
        assertThat(passwordValidator.isValid(" ", null)).isFalse();
        assertThat(passwordValidator.isValid(" a", null)).isFalse();
        assertThat(passwordValidator.isValid("a ", null)).isFalse();
        assertThat(passwordValidator.isValid("111", null)).isFalse();
        assertThat(passwordValidator.isValid("abc", null)).isFalse();
        assertThat(passwordValidator.isValid("abc12", null)).isTrue();
    }
}