package com.example.springvalidation;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinMemberService joinMemberService;

    @PostMapping("/members")
    public void joinMember(@RequestBody MemberJoinerRequest memberJoinerRequest) {

        //유효성 검증
        String id = memberJoinerRequest.getId();
        String password = memberJoinerRequest.getPassword();
        int age = memberJoinerRequest.getAge();

        if (StringUtils.hasText(id)) {
            throw new IllegalArgumentException("id가 공백입니다.");
        }

        if (StringUtils.hasText(password)) {
            throw new IllegalArgumentException("password가 공백입니다.");
        }

        if (age <= 0) {
            throw new IllegalArgumentException("age는 0보다 작을 수 없습니다.");
        }

        //회원가입 요청
        joinMemberService.joinMember(memberJoinerRequest);
    }

    @PostMapping("/members-valid")
    public void joinValidMember(@RequestBody @Valid MemberJoinerRequest request) {
        //회원가입 요청
        joinMemberService.joinMember(request);
    }

    @PostMapping("/members-validated")
    public void joinValidatedMember(@RequestBody MemberJoinerRequest request) {


        MemberJoinerCommend commend = new MemberJoinerCommend(request.getId(), request.getPassword(), request.getAge());
        //회원가입 요청
        joinMemberService.joinMember(commend);
    }

}
