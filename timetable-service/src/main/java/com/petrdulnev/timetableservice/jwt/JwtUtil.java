package com.petrdulnev.timetableservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    public ArrayList getRolesFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.get("roles", ArrayList.class);
    }

    public Claims getClaimsFromToken(String token) {
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }        JwtParser parser = Jwts.parser().verifyWith((SecretKey) key).build();
        return (Claims) parser.parse(token).getPayload();
    }

    @PostConstruct
    public void initKey() {
        this.key = new
                SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }
}
