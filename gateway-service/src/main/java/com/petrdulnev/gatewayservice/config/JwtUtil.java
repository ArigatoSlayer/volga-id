package com.petrdulnev.gatewayservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKeySpec key;

    @PostConstruct
    public void initKey() {
        this.key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

    public Claims getClaims(String token) {
        JwtParser parser = Jwts.parser().verifyWith(key).build();
        return (Claims) parser.parse(token).getPayload();
    }

    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
