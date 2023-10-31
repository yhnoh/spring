package com.example.springsecurityhello.security.authorization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void generateTokenTest(){
        long expireIn = 1000L * 60L * 60L;

        JwtService jwtService = new JwtService("xitrNXnuR1V5e9GLfZK32dA45mV2pom5", expireIn);
        String token = jwtService.generateToken("username");
        System.out.println(token);
    }

    @Test
    void isValidToken(){
        long expireIn = 1000L * 60L * 60L;
        JwtService jwtService = new JwtService("xitrNXnuR1V5e9GLfZK32dA45mV2pom5", expireIn);
        boolean validToken = jwtService.isValidToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY5ODc2MDM0NSwiZXhwIjoxNjk4NzYzOTQ1fQ.kLu_B4P5rBsVNLE7cfYzCDU5zKqLxU7QUlMp4KsMtuc");
        System.out.println(validToken);

    }
}