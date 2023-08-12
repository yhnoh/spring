package com.example.springvalidation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
@Validated
public class ValidateService {


    public void validate(@Valid ValidateCommand validateCommand) {

    }
}
