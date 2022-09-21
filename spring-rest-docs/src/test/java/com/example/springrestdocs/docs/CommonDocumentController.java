package com.example.springrestdocs.docs;

import com.example.springrestdocs.common.CustomResponse;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.io.Serializable;

@RestController
public class CommonDocumentController {

    @GetMapping("/docs/response")
    public CustomResponse<Object> customResponse() {
        return CustomResponse.success(null);
    }
}
