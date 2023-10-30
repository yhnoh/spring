package com.example.springsecurityhello.security.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {

    private final Key key;
    private final String secretKey;
    public JwtService(@Value(value = "${jwt.secret}") String secretKey) {

    }

    String createToken(){
        Algorithm algorithm = Algorithm.(rsaPublicKey, rsaPrivateKey);
        String token = JWT.create()
                .withIssuer("auth0")
                .sign(algorithm)
                ;
    }
    public String generateToken(String email, int expiredTime) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, email, expiredTime);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValidToken(String token) {

        Claims claims = extractAllClaims(token);
        return false;
    }

    private String createToken(HashMap<String, Object> claims, String email, int expiredTime) {

        return Jwts.builder()
                .setHeader(settingHeaders())
                .signWith(SignatureAlgorithm.HS512, )
                .signWith(key, SignatureAlgorithm.HS512)
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(settingsDate(0))
                .setExpiration(settingsDate(expiredTime))
                .compact();
    }

    private Date settingsDate(int plusTime) {
        return Date.from(
                LocalDateTime.now().plusMinutes(plusTime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    private Map<String, Object> settingHeaders() {
        return Map.of("typ", Header.JWT_TYPE, "alg", SignatureAlgorithm.HS512);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }}
