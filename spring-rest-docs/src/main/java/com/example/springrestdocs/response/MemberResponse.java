package com.example.springrestdocs.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class MemberResponse {
    private String userId;
    private String username;
}
