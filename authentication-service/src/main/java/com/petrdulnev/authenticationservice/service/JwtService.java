package com.petrdulnev.authenticationservice.service;

import com.petrdulnev.authenticationservice.model.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    public String generateToken(Account user) {
        LocalDateTime dateTime = LocalDateTime.now();
        Date issue = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date exp = Date.from(dateTime.plusHours(24).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .signWith(key)
                .issuedAt(issue)
                .expiration(exp)
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .compact();
    }

    public String generateRefreshToken() {
        LocalDateTime dateTime = LocalDateTime.now();
        Date issue = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date exp = Date.from(dateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .signWith(key)
                .issuedAt(issue)
                .expiration(exp)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public ArrayList getRolesFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.get("roles", ArrayList.class);
    }

    public Claims getClaimsFromToken(String token) {
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        JwtParser parser = Jwts.parser().verifyWith((SecretKey) key).build();
        return (Claims) parser.parse(token).getPayload();
    }

    public boolean isValidToken(String token) {
        Date deadTime = getClaimsFromToken(token).getExpiration();
        return (token != null) || !deadTime.before(new Date(System.currentTimeMillis()));
    }

    @PostConstruct
    public void initKey() {
        this.key = new
                SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

}
