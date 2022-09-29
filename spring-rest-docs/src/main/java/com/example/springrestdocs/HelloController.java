package com.example.springrestdocs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("hello-world")
    public Map<String, Object> helloWorld(){

        Map<String,Object> helloWorldData = new HashMap<>();
        helloWorldData.put("data", "hello-world");
        return helloWorldData;
    }
}
