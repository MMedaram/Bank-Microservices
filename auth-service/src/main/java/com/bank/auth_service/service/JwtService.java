package com.bank.auth_service.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    // ---------------------- VALIDATE TOKEN ----------------------
    public void validateToken(final String token) {
        Jwts.parser()
                .verifyWith(getSignKey())           // New API
                .build()
                .parseSignedClaims(token);          // New API
    }

    // ---------------------- GENERATE TOKEN ----------------------
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();

          return Jwts.builder()
                .claims(claims)                    // New API (instead of setClaims)
                .subject(userName)                 // New API (instead of setSubject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), Jwts.SIG.HS256)  // New API (instead of SignatureAlgorithm)
                .compact();
    }

    // ---------------------- SIGNING KEY ----------------------
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);        // Returns SecretKey
    }
}
