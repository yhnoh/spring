package com.example.springrestdocs.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
