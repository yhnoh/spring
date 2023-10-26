package com.example.springsecurityhello;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;


public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public RestUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login",
                "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        LoginRequest loginRequest = this.readLoginRequest(request);
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(),
                loginRequest.getPassword());

        return getAuthenticationManager().authenticate(authRequest);

    }

    LoginRequest readLoginRequest(HttpServletRequest request){
        try {
            return objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getHeader("username");
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getHeader("password");
    }
}
