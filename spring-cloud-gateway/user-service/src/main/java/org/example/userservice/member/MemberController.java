package org.example.userservice.member;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/sign-up")
    public void signUp(MemberSignUpRequest memberSignUpRequest) {
        memberService.signUp(memberSignUpRequest.getLoginId(),
                memberSignUpRequest.getLoginPassword(),
                memberSignUpRequest.getRole());
    }
}
