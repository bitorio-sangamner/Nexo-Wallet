package com.wallet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
//This class contains method related to perform operations with jwt token like generateToken, validateToken etc.
public class JwtHelper {

    private String secret = "e07042972c539f933588c40f3f0c5620c335c3687b6dadd6aac1f191c1b04d0a";


//    //requirement :
//    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
//
//    //    public static final long JWT_TOKEN_VALIDITY =  60;
//    private String secret = "e07042972c539f933588c40f3f0c5620c335c3687b6dadd6aac1f191c1b04d0a";
//
//    //retrieve username from jwt token
//    public String getUsernameFromToken(String token) {
//        return getClaimFromToken(token, Claims::getSubject);
//    }
//
//    //retrieve expiration date from jwt token
//    public Date getExpirationDateFromToken(String token) {
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = getAllClaimsFromToken(token);
//        return claimsResolver.apply(claims);
//    }
//
//    //for retrieveing any information from token we will need the secret key
//    private Claims getAllClaimsFromToken(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
////    private Claims getClaims(String token) {
////        return Jwts.parser().
////        setSigningKey(secret).parseClaimsJws(token).getBody();
////    }
//    //check if the token has expired
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    //generate token for user
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        return doGenerateToken(claims, userDetails.getUsername());
//    }
//
//    //while creating the token
//    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//    //2. Sign the JWT using the HS512 algorithm and secret key.
//    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//    //   compaction of the JWT to a URL-safe string
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//    }
//
//    //validate token
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }



 //*****************************************************************************************************


    /**
     * This method is used getting claims from a JWT.
     *
     * @param token JWT provided for authentication.
     * @return Claims user details from the JWT token.
     */
//    private Claims getClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * This method is used for extracting specific data from the claims obtained form JWT. This method uses Functional
     * programming for getting the desired claim by providing the specific function.
     *
     * @param token          JWT provided for authentication.
     * @param claimsResolver The function for getting the desired claim.
     * @param <T>            it is used to return any type of value (class or primitive) based on function provided.
     * @return the desired claim based on the function provided.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    /**
     * This method is used for extracting email from the claims extracted from the JWT.
     *
     * @param token JWT provided for authentication.
     * @return email address extracted from the claims.
     */
    public String claimsExtractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * This method is used for expiration time of JWT from the claims extracted from the JWT.
     *
     * @param token JWT provided for authentication.
     * @return Date the expiration time of the provided JWT for requests.
     */
    private Date claimsExtractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * This method is used to check whether the token has been expired.
     *
     * @param token JWT provided for authentication.
     * @return boolean true if token has been expired and false for not expired.
     */
    private boolean isTokenExpired(String token) {
        return claimsExtractExpiration(token).after(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = claimsExtractEmail(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

}