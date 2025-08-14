package com.citadel.apigateway.auth;

import com.citadel.apigateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private JwtParser jwtParser;

    @Autowired
    public JwtService(JwtConfig jwtConfig ){
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    public void init(){
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }


    public String extractUserEmail(String token){
            return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

    Claims getClaims(String token){
            return jwtParser.parseSignedClaims(token).getPayload();

    }


}
