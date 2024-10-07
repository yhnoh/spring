package org.example.userservice.security;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    public void createAccessToken() {

        String accessToken = jwtService.createAccessToken("username");
        //given
        JwtProperties jwtProperties = new JwtProperties();

    }
}