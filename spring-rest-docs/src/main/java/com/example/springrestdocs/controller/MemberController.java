package com.example.springrestdocs.controller;

import com.example.springrestdocs.common.CustomResponse;
import com.example.springrestdocs.request.MemberJoinRequest;
import com.example.springrestdocs.response.MemberJoinResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/members")
public class MemberController {

    @PostMapping
    public CustomResponse<MemberJoinResponse> joinMember(@RequestBody MemberJoinRequest memberJoinRequest){
        MemberJoinResponse memberJoinResponse = MemberJoinResponse.builder()
                .userId(memberJoinRequest.getUsername())
                .username(memberJoinRequest.getUsername())
                .build();
        return CustomResponse.success(memberJoinResponse);
    }

//    @PostMapping
//    public MemberJoinResponse joinMember(@RequestBody MemberJoinRequest memberJoinRequest){
//        return MemberJoinResponse.builder()
//                .userId(memberJoinRequest.getUsername())
//                .username(memberJoinRequest.getUsername())
//                .build();
//    }

}
