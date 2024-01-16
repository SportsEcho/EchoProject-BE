package com.sportsecho.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.common.jwt.exception.JwtErrorCode;
import com.sportsecho.common.oauth.exception.OAuthErrorCode;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberDetailsImpl;
import com.sportsecho.common.oauth.SocialType;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.mapper.MemberMapper;
import com.sportsecho.member.repository.MemberRepository;
import com.sportsecho.common.oauth.OAuthUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.TableGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.JsonPath;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * V2 업데이트 내용
 * - V1에서 builder로 생성하던 MemberEntity를 MemberMapper를 통해 생성
 * - V1에서 builder로 생성해 반환하던 MemberResponseDto를 MemberMapper를 통해 생성
 * */
@Service("V2")
@Slf4j(topic = "MemberServiceV2")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImplV2 implements MemberService {

    @Value("${admin.key.secret}")
    private String adminKey;

    private final MemberRepository memberRepository;

    private final JwtUtil jwtUtil;
    private final OAuthUtil oAuthUtil;
    private final RedisUtil redisUtil;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResponseDto signup(MemberRequestDto request, MemberRole role) {

        //email duplicate check
        if(memberRepository.findByEmail(request.getEmail()).isPresent())
            throw new GlobalException(MemberErrorCode.DUPLICATED_EMAIL);

        //MemberMapper를 이용한 Entity 생성
        Member member = MemberMapper.MAPPER.toEntity(request, role);
        member.passwordEncode(passwordEncoder);

        memberRepository.save(member);


        return MemberMapper.MAPPER.toResponseDto(member);
    }

    @Override
    public MemberResponseDto adminSignup(MemberRequestDto request, String key) {
        if(adminKey.equals(key)) {
            return signup(request, MemberRole.ADMIN);
        } else {
            throw new GlobalException(MemberErrorCode.INVALID_ADMIN_KEY);
        }
    }

    @Override
    public void login(MemberRequestDto request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Member member = ((MemberDetailsImpl) authentication.getPrincipal()).getMember();

            String accessToken = jwtUtil.generateAccessToken(member.getEmail(), member.getRole());
            String refreshToken = jwtUtil.generateRefreshToken();

            //ResponseHeader에 토큰 추가
            jwtUtil.setJwtHeader(response, accessToken, refreshToken);

            //Redis에 refreshToken 저장
            redisUtil.saveRefreshToken(refreshToken, member.getEmail());

        } catch(BadCredentialsException e) {
            throw new GlobalException(MemberErrorCode.INVALID_AUTH);
        }
    }

    @Override
    public void logout(Member member, HttpServletRequest request) {
        String refreshToken = jwtUtil.getRefreshToken(request);

        //redis에 refreshToken이 존재하는 경우
        if(redisUtil.isExist(refreshToken)) {

            //현재 접속중인 사용자가 로그아웃을 요청한 사용자인지 확인
            if(member.getEmail().equals(redisUtil.getEmail(refreshToken))) {
                redisUtil.removeToken(refreshToken);
            } else {
                throw new GlobalException(MemberErrorCode.INVALID_REQUEST);
            }
        } else {
            throw new GlobalException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshToken(request);

        //redis에 refreshToken이 존재하는 경우
        if(redisUtil.isExist(refreshToken)) {
            Member member = memberRepository.findByEmail(redisUtil.getEmail(refreshToken))
                .orElseThrow(() -> new GlobalException(MemberErrorCode.USER_NOT_FOUND));

            //accessToken 발급
            String accessToken = jwtUtil.generateAccessToken(member.getEmail(), member.getRole());

            jwtUtil.setJwtHeader(response, accessToken, refreshToken);
        } else {
            throw new GlobalException(JwtErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public MemberResponseDto deleteMember(Member member) {
        memberRepository.delete(member);
        return MemberMapper.MAPPER.toResponseDto(member);
    }

    @Override
    @Transactional
    public void kakaoLogin(String code, HttpServletResponse response) {
        //사용자 정보에 접근하기 위한 접근토큰 요청
        URI tokenUri = URI.create("https://kauth.kakao.com/oauth/token");
        JsonNode tokenJsonNode = oAuthUtil.getToken(tokenUri, SocialType.KAKAO, code);
        String accessToken = tokenJsonNode.get("access_token").asText();

        //접근토큰으로 사용자 정보 요청
        URI infoUri = URI.create("https://kapi.kakao.com/v2/user/me");
        JsonNode jsonNode = oAuthUtil.getMemberInfo(infoUri, accessToken);

        Long kakaoId = jsonNode.get("id").asLong();
        String memberName = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        //Member객체를 이용해 서비스에 저장
        Member socialMember = oAuthUtil.registerSocialMemberIfNeeded(kakaoId, memberName, email, SocialType.KAKAO);

        String aToken = jwtUtil.generateAccessToken(socialMember.getEmail(), socialMember.getRole());
        String rToken = jwtUtil.generateRefreshToken();
        jwtUtil.setJwtHeader(response, aToken, rToken);
    }

    @Override
    @Transactional
    public void naverLogin(String code, HttpServletResponse response) {
        //사용자 정보에 접근하기 위한 접근토큰 요청
        URI tokenUri = URI.create("https://nid.naver.com/oauth2.0/token");
        JsonNode tokenJsonNode = oAuthUtil.getToken(tokenUri, SocialType.NAVER, code);
        String accessToken = tokenJsonNode.get("access_token").asText();

        //접근토큰으로 사용자 정보 요청
        URI infoUri = URI.create("https://openapi.naver.com/v1/nid/me");
        JsonNode jsonNode = oAuthUtil.getMemberInfo(infoUri, accessToken);

        //naver는 id값을 제공하지 않는다. 0으로 처리
        String memberName = jsonNode.get("response").get("name").asText();
        String email = jsonNode.get("response").get("email").asText();

        //Member객체를 이용해 서비스에 저장
        Member socialMember = oAuthUtil.registerSocialMemberIfNeeded(0L, memberName, email, SocialType.NAVER);

        String aToken = jwtUtil.generateAccessToken(socialMember.getEmail(), socialMember.getRole());
        String rToken = jwtUtil.generateRefreshToken();
        jwtUtil.setJwtHeader(response, aToken, rToken);
    }

    @Override
    @Transactional
    public void googleLogin(String code, HttpServletResponse response) {
        //사용자 정보에 접근하기 위한 접근토큰 요청
        URI tokenUri = URI.create("https://oauth2.googleapis.com/token");
        JsonNode tokenJsonNode = oAuthUtil.getToken(tokenUri, SocialType.GOOGLE, code);

        //Google 사용자 정보를 담고 있는 jwt 파싱
        String idToken = tokenJsonNode.get("id_token").asText();

        //idToken 페이로드 디코딩
        byte[] bytes = Base64.getDecoder().decode(idToken.split("\\.")[1]);
        String decodedIdToken = new String(bytes, StandardCharsets.UTF_8);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(decodedIdToken);

            //objectMapper로 필요한 데이터 파싱
            String email = jsonNode.get("email").asText();
            String memberName = jsonNode.get("name").asText();

            Member socialMember = oAuthUtil.registerSocialMemberIfNeeded(0L, memberName, email, SocialType.GOOGLE);

            String aToken = jwtUtil.generateAccessToken(socialMember.getEmail(), socialMember.getRole());
            String rToken = jwtUtil.generateRefreshToken();
            jwtUtil.setJwtHeader(response, aToken, rToken);
        } catch(Exception e) {
            throw new GlobalException(OAuthErrorCode.ILLEGAL_REQUEST);
        }
    }
}
