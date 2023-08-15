package com.example.springvalidation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
@Validated
public class JoinMemberService {

    public void joinMember(MemberJoinerRequest memberJoinerRequest) {


    }

    public void joinMember(@Valid MemberJoinerCommend memberJoinerCommend) {


    }

}
