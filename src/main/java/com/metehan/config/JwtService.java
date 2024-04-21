package com.metehan.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SIGNIN_KEY = "+7KRgBbmj0M9F3r9MVcFLo0OqREhE6ClNzZQ3muUyp/+zS78jBx2WyRxXjU6otv/";

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    public boolean isTokenExpired(String token) {
        return  extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Key getKey(){
        byte[] key = Decoders.BASE64.decode(SIGNIN_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public Claims extractClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)      //this method implementation also checks if jwt expired or not
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    //generates token only with user name
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //generates token with claims
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.
                builder().
                setClaims(extraClaims).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date((System.currentTimeMillis()))).
                setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)).
                signWith(getKey(), SignatureAlgorithm.HS256).
                compact();
    }


}
