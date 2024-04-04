package com.authentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private String expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void initKey() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    public String claimsExtractUsername(String token) { return extractClaim(token, Claims::getSubject); }

    private Date claimsExtractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }

    private boolean isTokenExpired(String token) {
        log.info("Expiration date - {} and current Date is - {}", claimsExtractUsername(token), new Date());
        return claimsExtractExpiration(token).before(new Date());
    }

    public String generate(String email, String role, String tokenType) {
        Map<String, String> claims = Map.of("email", email, "role", role);
        long expirationInMilliSeconds = Long.parseLong(expiration) * 1000;

        final Date NOW = new Date();
        final Date EXP = new Date(NOW.getTime() + expirationInMilliSeconds);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(NOW)
                .expiration(EXP)
                .signWith(secretKey)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = claimsExtractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
