package com.vaishnav.medsync.users.security.jwt;

import com.vaishnav.medsync.users.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    // Getting JWT Token form Authorization Header
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    // Generating Actual JWT Token
    public String generateJwtToken(UserDetailsImpl userDetailsImpl){
        String username = userDetailsImpl.getUsername();
        String roles = userDetailsImpl.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining());
        boolean isEmailVerified = userDetailsImpl.isEmailVerified();
        return Jwts.builder()
                .subject(username)
                .claim("roles",roles)
                .claim("isEmailVerified",isEmailVerified)
                .claim("isActive",userDetailsImpl.isActive())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getKey())
                .compact();
    }

    // Extracting Username(here email) from JWT Token
    public String getUsernameFromJWT(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    // Validate token if matches JWT Secret
    public Boolean validateToken(String authToken){
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
