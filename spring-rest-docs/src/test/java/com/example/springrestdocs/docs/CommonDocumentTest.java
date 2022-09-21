package com.example.springrestdocs.docs;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.payload.AbstractFieldsSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommonDocumentController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ActiveProfiles("test")
public class CommonDocumentTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 공통 응답 처리에 대한 docs
     */
    @Test
    public void commonResponse() throws Exception {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("title", "공통응답");


        mockMvc.perform(MockMvcRequestBuilders.get("/docs/response")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("common/response")
                        .document(commonResponseFields("common-response",
                                null, attributes,
                                fieldWithPath("status").description("응답 코드"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("message").description("응답 메시지")
                                ))
                        );
    }

    //
    public static CommonResponseFieldsSnippet commonResponseFields(String type,
                                                                   PayloadSubsectionExtractor<?> subsectionExtractor,
                                                                   Map<String,Object> attributes,
                                                                   FieldDescriptor... descriptors){
        return new CommonResponseFieldsSnippet(type, Arrays.asList(descriptors), attributes, true, subsectionExtractor);
    }


    /**
     * response-fields.snippet이 제작되어 있으면, response-fields.snippet 포맷에 맞게 docs가 생성되기 때문에
     * 특정 이름의 스니펫을 사용할 수 있게 CommonResponseFieldsSnippet 제작
     */
    public static class CommonResponseFieldsSnippet extends AbstractFieldsSnippet{

        protected CommonResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields) {
            super(type, descriptors, attributes, ignoreUndocumentedFields);
        }

        protected CommonResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields, PayloadSubsectionExtractor<?> subsectionExtractor) {
            super(type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
        }

        protected CommonResponseFieldsSnippet(String name, String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields) {
            super(name, type, descriptors, attributes, ignoreUndocumentedFields);
        }

        protected CommonResponseFieldsSnippet(String name, String type, List<FieldDescriptor> descriptors, Map<String, Object> attributes, boolean ignoreUndocumentedFields, PayloadSubsectionExtractor<?> subsectionExtractor) {
            super(name, type, descriptors, attributes, ignoreUndocumentedFields, subsectionExtractor);
        }

        @Override
        protected MediaType getContentType(Operation operation) {
            return operation.getResponse().getHeaders().getContentType();
        }

        @Override
        protected byte[] getContent(Operation operation) throws IOException {
            return operation.getResponse().getContent();
        }
    }
}