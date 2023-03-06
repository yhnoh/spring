package com.example.springmsaresilience4j;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private Long id;
    private String username;
    private String password;
}
