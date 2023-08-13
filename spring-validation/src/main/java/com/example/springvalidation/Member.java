package com.example.springvalidation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Member {
    private final String id;
    private final String password;
    private final int age;
}
