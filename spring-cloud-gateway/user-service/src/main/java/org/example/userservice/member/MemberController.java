package org.example.userservice.member;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberResponseMapper memberResponseMapper;

    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable long id) {
        return memberService.getMember(id).map(memberResponseMapper::toMemberResponse)
                .orElse(null);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody MemberSignUpRequest memberSignUpRequest) {
        memberService.signUp(memberSignUpRequest.getLoginId(),
                memberSignUpRequest.getLoginPassword(),
                memberSignUpRequest.getRole());
    }
}
