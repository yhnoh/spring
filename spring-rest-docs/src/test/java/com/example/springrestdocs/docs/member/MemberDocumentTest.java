package com.example.springrestdocs.docs.member;

import com.example.springrestdocs.controller.MemberController;
import com.example.springrestdocs.docs.ApiDocumentUtils;
import com.example.springrestdocs.request.MemberChangePasswordRequest;
import com.example.springrestdocs.request.MemberJoinRequest;
import com.example.springrestdocs.response.MemberResponse;
import com.example.springrestdocs.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class MemberDocumentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    public void findMembers() throws Exception {
        //given
        List<MemberResponse> members = Arrays.asList(
                MemberResponse.builder()
                        .userId("userId1")
                        .username("username1")
                        .build(),
                MemberResponse.builder()
                        .userId("userId2")
                        .username("username2")
                        .build()
        );
        given(memberService.findMembers()).willReturn(members);

        //when
        ResultActions perform = mockMvc.perform(get("/v1/members")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        perform.andExpect(status().isOk())
                .andDo(ApiDocumentUtils.document(
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("username").description("이름")
                        )
                ));
    }

    @Test
    public void findMember() throws Exception {
        //give
        given(memberService.findMember(any()))
                .willReturn(MemberResponse.builder()
                        .userId("userId")
                        .username("username")
                        .build());
        //when
        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/members/{userId}", "userId")
                        .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpect(status().isOk())
                .andDo(ApiDocumentUtils.document(
                        pathParameters(
                                parameterWithName("userId").description("회원 ID")
                        )
                        , responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("userId").description("회원 ID"),
                                fieldWithPath("username").description("회원 이름")
                        )
                ));
    }

    @Test
    public void joinMember() throws Exception {
        //given
        given(memberService.joinMember(any())).willReturn(MemberResponse.builder()
                .userId("아이디")
                .username("이름")
                .build());

        //when
        MemberJoinRequest request = new MemberJoinRequest("아이디", "이름", "비밀번호");
        ResultActions perform = mockMvc.perform(post("/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(ApiDocumentUtils.document(
                        requestFields(
                                fieldWithPath("userId").description("회원 ID"),
                                fieldWithPath("username").description("회원 이름"),
                                fieldWithPath("password").description("패스워드")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("userId").description("회원 ID"),
                                fieldWithPath("username").description("회원 이름")
                        )
                ));
    }

    @Test
    public void changePassword() throws Exception {
        //given
        given(memberService.changePassword(any(), any()))
                .willReturn(true);

        //when
        MemberChangePasswordRequest request = new MemberChangePasswordRequest();
        request.setOldPassword("old");
        request.setNewPassword("new");

        ResultActions perform = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/v1/members/{userId}", "userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //then
        perform.andExpect(status().isOk())
                .andDo(ApiDocumentUtils.document(
                        pathParameters(
                                parameterWithName("userId").description("회원 ID")
                        ),
                        requestFields(
                                fieldWithPath("oldPassword").description("이전 패스워드"),
                                fieldWithPath("newPassword").description("새로운 패스워드")
                        )
                ));

    }

    @Test
    public void deleteMember() throws Exception {
        //given
        given(memberService.deleteMember(any(), any()))
                .willReturn(true);

        //when
        ResultActions perform = mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/v1/members/{userId}", "userId")
                .queryParam("password", "password")
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andExpect(status().isOk())
                .andDo(ApiDocumentUtils.document(
                        pathParameters(
                                parameterWithName("userId").description("회원 아이디")
                        ),
                        requestParameters(
                                parameterWithName("password").description("password")
                        )
                ));
    }
}
