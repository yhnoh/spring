package org.example.helloworld;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationHeaderFilterTest {

    @Test
    public void validJwtTest() {
        //given
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.svxBzrTOSO4sPVwPWqWq7k59SOZzhsHl60WJp1t8Kfg";
//        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.mFfnNtm7avaD-MouLptQGvQqpg86nh7iCSyv2NJpZRY";
        //when


        SecretKey secretKey = Keys.hmacShaKeyFor("jwtsigntutorialasdfasdfasdfasdfasdf".getBytes(StandardCharsets.UTF_8));
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        Claims body = jwtParser.parseClaimsJws(jwt).getBody();
        //then
    }

}