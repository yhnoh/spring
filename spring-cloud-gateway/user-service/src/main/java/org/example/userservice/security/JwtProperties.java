package org.example.userservice.security;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JwtProperties {

    private String secret;
    private long accessTokenExpiration;
}
