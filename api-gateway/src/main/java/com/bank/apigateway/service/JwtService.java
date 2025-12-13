package com.bank.apigateway.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

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

    // ---------------------- SIGNING KEY ----------------------
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);        // Returns SecretKey
    }
}
