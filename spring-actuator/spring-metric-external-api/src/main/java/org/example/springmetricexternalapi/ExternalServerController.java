package org.example.springmetricexternalapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/exception")
    public ResponseEntity<String> exception() {
        throw new RuntimeException("RuntimeException");
    }

    @GetMapping("/error/400")
    public ResponseEntity<String> error400() {
        return ResponseEntity.badRequest().body("Bad Request");
    }

    @GetMapping("/error/500")
    public ResponseEntity<String> error500() {
        return ResponseEntity.status(500).body("Internal Server Error");
    }



    public abstract class Abstract
    public static class Order {

    }

}
