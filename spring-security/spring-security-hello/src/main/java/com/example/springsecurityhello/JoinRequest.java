package com.example.springsecurityhello;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class JoinRequest {

    private String username;
    private String password;
    public JoinRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
