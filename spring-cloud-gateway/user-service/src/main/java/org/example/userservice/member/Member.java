package org.example.userservice.member;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Member {

    private long id;
    private String loginId;
    private String loginPassword;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public void signUp(String username, String password, String role) {
        this.loginId = username;
        this.loginPassword = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}
