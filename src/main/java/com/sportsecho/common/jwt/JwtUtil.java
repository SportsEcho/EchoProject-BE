package com.sportsecho.common.jwt;


import com.sportsecho.global.exception.ErrorCode;
import com.sportsecho.global.exception.GlobalException;
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

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_AUTHORIZATION_HEADER = "RefreshAuthorization";

    private final String BEARER_PREFIX = "Bearer ";
    private final String AUTHORIZATION_KEY = "auth";
    private final Long ACCESS_TIME = 60 * 60 * 1000L;
    private final Long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret_key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(String email, MemberRole role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESS_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String generateRefreshToken() {
        Date date = new Date();

        //refresh token 내부에는 사용자의 정보를 담지 않는다.
        return BEARER_PREFIX +
            Jwts.builder()
                .setExpiration(new Date(date.getTime() + REFRESH_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String getAccessToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    public String getRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_AUTHORIZATION_HEADER);
    }

    public String substringToken(String tokenValue) {
        if(tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }

        //tokenValue가 Bearer로 시작하지 않는 경우
        throw new GlobalException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new GlobalException(ErrorCode.SIGNATURE_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new GlobalException(ErrorCode.EXPIRED_TOKEN_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new GlobalException(ErrorCode.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    public String getSubject(String token) {
        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        //return member email
        return body.getSubject();
    }
}
