package org.example.userservice.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MemberResponse {

    private long id;
    private String loginId;
    private String loginPassword;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
