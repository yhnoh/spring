package com.example.springrestdocs.controller;

import com.example.springrestdocs.common.CustomResponse;
import com.example.springrestdocs.request.MemberJoinRequest;
import com.example.springrestdocs.request.MemberChangePasswordRequest;
import com.example.springrestdocs.response.MemberResponse;
import com.example.springrestdocs.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;

    @GetMapping
    public CustomResponse<List<MemberResponse>> findMembers(){
        return CustomResponse.success(memberService.findMembers());
    }

    @GetMapping("/{userId}")
    public CustomResponse<MemberResponse> findMember(@PathVariable String userId){
        return CustomResponse.success(memberService.findMember(userId));
    }

    @PostMapping
    public CustomResponse<MemberResponse> joinMember(@RequestBody MemberJoinRequest memberJoinRequest){
        MemberResponse data = memberService.joinMember(memberJoinRequest);
        return CustomResponse.success(data);
    }

    @PutMapping("/{userId}")
    public CustomResponse<Boolean> changePassword(@PathVariable String userId, @RequestBody MemberChangePasswordRequest memberChangePasswordRequest){
        return CustomResponse.success(memberService.changePassword(userId, memberChangePasswordRequest));
    }

    @DeleteMapping("/{userId}")
    public CustomResponse<Boolean> deleteMember(@PathVariable String userId, @RequestParam String password){
        return CustomResponse.success(memberService.deleteMember(userId, password));
    }


}
