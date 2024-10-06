package org.example.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class MemberAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    protected MemberAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, ObjectMapper objectMapper) {
        super(requiresAuthenticationRequestMatcher);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        MemberAuthenticationRequest memberAuthenticationRequest = objectMapper.readValue(messageBody, MemberAuthenticationRequest.class);
        
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberAuthenticationRequest.getUsername(),
                memberAuthenticationRequest.getPassword());

        return getAuthenticationManager().authenticate(token);
    }
}
