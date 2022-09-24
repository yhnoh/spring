package com.example.springrestdocs.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
public class Member {
    private String userId;
    private String username;
    private String password;

}
