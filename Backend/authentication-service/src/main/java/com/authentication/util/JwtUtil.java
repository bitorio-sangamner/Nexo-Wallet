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

/**
 * This class is used for JWT generation and validation.
 * @author rsmalani
 */
@Service
@Slf4j
public class JwtUtil {

    /**
     *  JWT private key
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Expiration time in seconds
     */
    @Value("${jwt.expiration}")
    private String expiration;

    /**
     * Secret key for JWT authentication
     */
    private SecretKey secretKey;

    /**
     * This method is used for initializing secret key with @PostConstruct annotation
     */
    @PostConstruct
    public void initKey() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * This method is used getting claims from a JWT.
     * @param token JWT provided for authentication.
     * @return Claims user details from the JWT token.
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * This method is used for extracting specific data from the claims obtained form JWT. This method uses Functional
     * programming for getting the desired claim by providing the specific function.
     * @param token JWT provided for authentication.
     * @param claimsResolver The function for getting the desired claim.
     * @return the desired claim based on the function provided.
     * @param <T> it is used to return any type of value (class or primitive) based on function provided.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }


    public String claimsExtractUsername(String token)
    { return extractClaim(token, Claims::getSubject);
    }
    /**
     * This method is used for extracting email from the claims extracted from the JWT.
     * @param token JWT provided for authentication.
     * @return email address extracted from the claims.
     */
    public String claimsExtractEmail(String token) { return extractClaim(token, Claims::getSubject); }


    /**
     * This method is used for expiration time of JWT from the claims extracted from the JWT.
     * @param token JWT provided for authentication.
     * @return Date the expiration time of the provided JWT for requests.
     */
    private Date claimsExtractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }

    /**
     * This method is used to check whether the token has been expired.
     * @param token JWT provided for authentication.
     * @return boolean true if token has been expired and false for not expired.
     */
    private boolean isTokenExpired(String token) {
        return claimsExtractExpiration(token).after(new Date());
    }

    /**
     * This method is used to generate JWT for user with help of email, role and type of token. The token type is not
     * used but as a blueprint for future improvement for JWT generation.
     * @param email Email address of user.
     * @param role Role of the user.
     * @param tokenType Token type for JWT.
     * @return JWT token generated with help of email, role, tokenType, issued time, expiration time and secret key.
     */
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

    /**
     * This method is used for the validation of the JWT.
     * @param token JWT
     * @param userDetails user details of the user provided with JWT.
     * @return true if correct token and false for incorrect.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = claimsExtractEmail(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

}
