package com.sportsecho.common.jwt;


import com.sportsecho.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j(topic = "JwtUtil")
@Service
public class JwtUtil {

    private final String BEARER_PREFIX = "Bearer ";
    private final String AUTHORIZATION_KEY = "auth";

    private final Long TOKEN_TIME = 60 * 60 * 1000L;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret_key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String email, MemberRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization.substring(BEARER_PREFIX.length());
    }

//    public Claims getAllClaims(String token) {
//        try {
//            return Jwts
//                .parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        } catch (SecurityException | MalformedJwtException | SignatureException e) {
//            log.error("ERROR: {}", "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
//        } catch (ExpiredJwtException e) {
//            log.error("ERROR: {}", "Expired JWT token, 만료된 JWT token 입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.error("ERROR: {}", "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
//        } catch (IllegalArgumentException e) {
//            log.error("ERROR: {}", "JWT claims is empty, 잘못된 JWT 토큰 입니다.");
//        }
//    }

}
