package com.example.springrestdocs.docs;

import com.example.springrestdocs.common.CustomResponse;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

@RestController
public class CustomDocumentController {

    @GetMapping("/docs/response")
    public CustomResponse<Object> customResponse() {
        return CustomResponse.success(new Object());
    }
}
