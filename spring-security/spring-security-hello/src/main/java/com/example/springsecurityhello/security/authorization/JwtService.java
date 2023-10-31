package com.example.springsecurityhello.security.authorization;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtService {

    private final String secretKey;
    private final Long expireIn;

    public JwtService(@Value(value = "${jwt.secret}") String secretKey, @Value(value = "${jwt.expire-in}") Long expireIn) {
        this.secretKey = secretKey;
        this.expireIn = expireIn;
    }

    String generateToken(String subject) {
        SecretKey key = getSecretKey();

        Date date = new Date();
        return Jwts.builder()
                .header().add("alg", "HS256").add("typ", "JWT").and()
                .subject(subject)
                .issuedAt(date)
                .expiration(new Date(date.getTime() + expireIn))
                .signWith(key)
                .compact();
    }

    boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.getSecretKey()) // <----
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

}


