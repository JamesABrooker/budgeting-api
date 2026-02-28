package com.jamesbrooker.budgeting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private final String secretKey = System.getenv("JWT_SECRET");
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    // Validate the token (signature + expiration)
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            // Check that claims exist in database
            Date expiration = claims.getExpiration();
            if(expiration.before(new Date())) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract claims from token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createToken(String userId, String email) {
        //Defines claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);

        //Defines Token expiration
        long expirationTimeMillis = 1000 * 60 * 60 * 24; // 24 hours
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMillis);


        //Builds the token
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
