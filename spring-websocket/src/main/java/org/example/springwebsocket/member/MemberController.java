package org.example.springwebsocket.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberUseCase memberUseCase;

    @PostMapping("/member/sign-up")
    public void signUp(@RequestBody @Valid MemberSignUpRequest request) {
        memberUseCase.signUp(request.getEmail(), request.getNickname());
    }

}
