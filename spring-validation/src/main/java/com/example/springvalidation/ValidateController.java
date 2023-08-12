package com.example.springvalidation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ValidateController {

    private final ValidateService validateService;

    @PostMapping("/valid")
    public String valid(@RequestBody @Valid MemberJoinerRequest memberJoinerRequest) {
        return "ok";
    }

    @GetMapping("/validated")
    public String validated(@RequestParam(required = false) String id,
                            @RequestParam(required = false) String password,
                            @RequestParam(required = false) int age) {
        validateService.validate(new ValidateCommand(id, password, age));
        return "ok";
    }


    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
