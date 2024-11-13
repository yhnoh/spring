package org.example.proxiedservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

    @GetMapping
    public String rootEndPoint() {
        return "root endpoint";
    }


    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }
}
