package com.example.springrestdocs.docs.member;

import com.example.springrestdocs.controller.MemberController;
import com.example.springrestdocs.docs.ApiDocumentUtils;
import com.example.springrestdocs.request.MemberJoinRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class MemberDocumentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RestDocumentationResultHandler document;

    @BeforeEach
    public void setup(){
    }

    @Test
    public void joinMember() throws Exception {

        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("아이디", "이름", "비밀번호");

        ResultActions perform = mockMvc.perform(post("/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberJoinRequest)));

        perform.andExpect(status().isOk())
                .andDo(ApiDocumentUtils.document(
                        requestFields(
                                fieldWithPath("userId").description("회원 ID"),
                                fieldWithPath("username").description("회원 이름"),
                                fieldWithPath("password").description("패스워드")
                        ),
                        responseFields(
                                fieldWithPath("userId").description("회원 ID"),
                                fieldWithPath("username").description("회원 이름")
                        )
                        ));
    }
}
