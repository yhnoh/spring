package com.example.springrestdocs.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequest {

    private String userId;
    private String username;
    private String password;
}
