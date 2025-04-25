package com.example.h2_restful.service.user.ott;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService implements InitializingBean {

    private Key secretKey;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void afterPropertiesSet() throws Exception {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(String subject) {

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 500)) // um mês
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractSubject(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Date extractExpirationDate(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean isValid(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isExpired(String token) {
        // todo:
        return false;
    }
}
