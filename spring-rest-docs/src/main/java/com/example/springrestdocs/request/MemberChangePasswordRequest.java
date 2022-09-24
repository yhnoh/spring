package com.example.springrestdocs.request;

import lombok.Getter;

@Getter
public class MemberChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
