package org.example.springwebsocket.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberSignUpRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String nickname;
}
