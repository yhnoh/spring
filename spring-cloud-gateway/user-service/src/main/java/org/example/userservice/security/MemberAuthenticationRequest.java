package org.example.userservice.security;

import lombok.Getter;

@Getter
public class MemberAuthenticationRequest {

    private String username;
    private String password;
}
