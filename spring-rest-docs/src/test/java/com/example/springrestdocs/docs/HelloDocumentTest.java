package com.example.springrestdocs.docs;

import com.example.springrestdocs.HelloController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(HelloController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class HelloDocumentTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void helloWorldTest() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/hello-world")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcRestDocumentation.document("hello-world", PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("data").description("hello-world")
        )));
    }

    @Test
    public void helloWorld() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/hello-world")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(ApiDocumentUtils.document(PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("data").description("hello-world")
        )));
    }
}
