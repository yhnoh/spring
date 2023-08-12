package com.example.springvalidation;

import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RequiredArgsConstructor
public class ValidateCommand {

    @NotBlank
    @Size(min = 2, max = 10)
    private final String id;

    @NotBlank
    @Size(min = 2, max = 10)
    private final String password;

    @Min(19)
    private final int age;
}
