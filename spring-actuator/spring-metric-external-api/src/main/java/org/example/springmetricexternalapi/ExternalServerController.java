package org.example.springmetricexternalapi;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalServerController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/status/200")
    public ResponseEntity<ExternalServerResponse> success() {
        return ResponseEntity.ok().body(ExternalServerResponse.createSuccess());
    }

    @GetMapping("/status/200/error")
    public ResponseEntity<ExternalServerResponse> successError() {
        return ResponseEntity.ok().body(ExternalServerResponse.createError());
    }

    @GetMapping("/status/400")
    public ResponseEntity<ExternalServerResponse> error400() {
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/status/500")
    public ResponseEntity<ExternalServerResponse> error500() {
        return ResponseEntity.status(500).build();
    }



    @Data
    public static class ExternalServerResponse {
        private String result;

        public static ExternalServerResponse createSuccess() {
            ExternalServerResponse externalServerResponse = new ExternalServerResponse();
            externalServerResponse.setResult("success");
            return externalServerResponse;
        }

        public static ExternalServerResponse createError() {
            ExternalServerResponse externalServerResponse = new ExternalServerResponse();
            externalServerResponse.setResult("error");
            return externalServerResponse;
        }

    }

}
