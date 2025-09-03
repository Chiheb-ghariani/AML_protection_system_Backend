package com.aml.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JwtUtil {

    // Example 256-bit base64 secret. Replace with yours if you already set one.
    private static final String SECRET = "uGm8m8w2k9rH0n4tq3V0x5Q1y7Z9d2L6c1B8m3N6p4R7s0T2u5V8w1Y3z6K9L2M4";
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parse(token).getBody());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            parse(token); // will throw if invalid/expired
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
