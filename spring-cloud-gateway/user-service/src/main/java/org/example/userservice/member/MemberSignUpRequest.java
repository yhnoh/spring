package org.example.userservice.member;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberSignUpRequest {

    private String loginId;
    private String loginPassword;
    private String role;

}
