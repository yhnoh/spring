package com.example.springvalidation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class MemberJoinerRequest {

    @NotBlank
    private final String id;

    @NotBlank
    private final String password;

    @Min(1)
    private final int age;


}
