package com.example.springrestdocs.docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomDocumentController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class CustomDocumentTest {

    @Autowired
    private MockMvc mockMvc;

    public void commons(){
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("common/response").document(new CustomResponseFieldsSnippet(
                    "custom-response", null, attributes() ,
                )));


    }


    public static class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {



        public CustomResponseFieldsSnippet(String name, String type,
                                           List<FieldDescriptor> descriptors,
                                           Map<String, Object> attributes,
                                           boolean ignoreUndocumentedFields) {
            super(name, type, descriptors, attributes, ignoreUndocumentedFields);
        }

/*
        public CustomResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields) {
            super(type, descriptors, attributes, ignoreUndocumentedFields);
        }

        public CustomResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields, PayloadSubsectionExtractor<?> subsectionExtractor) {
            super(type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
        }

        public CustomResponseFieldsSnippet(String name, String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields, PayloadSubsectionExtractor<?> subsectionExtractor) {
            super(name, type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
        }
*/

        @Override
        protected MediaType getContentType(Operation operation) {
            return null;
        }

        @Override
        protected byte[] getContent(Operation operation) throws IOException {
            return new byte[0];
        }
    }

}
