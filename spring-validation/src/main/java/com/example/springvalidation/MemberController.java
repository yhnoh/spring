package com.example.springvalidation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final JoinMemberService joinMemberService;

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseEntity<CustomResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        CustomResponse customResponse = new CustomResponse(HttpStatus.BAD_REQUEST.value(), e);

        return ResponseEntity.status(customResponse.getStatus()).body(customResponse);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    private ResponseEntity constraintViolationExceptionHandler(ConstraintViolationException e) {
        CustomResponse customResponse = new CustomResponse(HttpStatus.BAD_REQUEST.value(), e);
        return ResponseEntity.status(customResponse.getStatus()).body(customResponse);
    }


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
